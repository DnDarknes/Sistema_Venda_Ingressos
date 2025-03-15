package com.exemplo.ingressos.service;

import com.exemplo.ingressos.model.Compra;
import com.exemplo.ingressos.model.Ingresso;
import com.exemplo.ingressos.model.IngressoUnico;
import com.exemplo.ingressos.repository.CompraRepository;
import com.exemplo.ingressos.repository.IngressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.exemplo.ingressos.model.IngressoUnico;
import com.exemplo.ingressos.repository.IngressoUnicoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class VendaIngressoService {

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final IngressoUnicoRepository ingressoUnicoRepository;

    public VendaIngressoService(IngressoUnicoRepository ingressoUnicoRepository) {
        this.ingressoUnicoRepository = ingressoUnicoRepository;
    }

    public String comprarIngresso(Long ingressoId, String compradorNome, String compradorEmail, Integer quantidade) {
        Ingresso ingresso = ingressoRepository.findById(ingressoId).orElse(null);
        if (ingresso == null) {
            return "Ingresso não encontrado.";
        }

        if (!ingresso.getStatus().equalsIgnoreCase("disponível")) {
            return "Ingressos não disponíveis para venda.";
        }

        if (quantidade > ingresso.getQuantidade()) {
            return "Quantidade de ingressos solicitada maior que a disponível.";
        }

        Compra compra = new Compra();
        compra.setIngresso(ingresso);
        compra.setCompradorNome(compradorNome);
        compra.setCompradorEmail(compradorEmail);
        compra.setQuantidade(quantidade);
        compra.setTotal(ingresso.getPreco() * quantidade);
        compra.setDataCompra(LocalDateTime.now());

        List<IngressoUnico> ingressosUnicos = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            IngressoUnico ingressoUnico = new IngressoUnico();
            ingressoUnico.setCompra(compra);

            // Gera um código único para o ingresso
            String codigoUnico = UUID.randomUUID().toString();
            ingressoUnico.setCodigo(codigoUnico); // Aqui, o código é atribuído

            ingressosUnicos.add(ingressoUnico);
        }
        compra.setIngressosUnicos(ingressosUnicos);

        compraRepository.save(compra);

        ingresso.setQuantidade(ingresso.getQuantidade() - quantidade);
        if (ingresso.getQuantidade() == 0) {
            ingresso.setStatus("vendido");
        }
        ingressoRepository.save(ingresso);

        enviarEmailConfirmacaoCompra(compradorEmail, ingresso.getEvento().getNome(), quantidade);

        return "Compra realizada com sucesso!";
    }

    public String validarIngresso(String codigoIngresso) {
        Optional<IngressoUnico> ingressoOpt = ingressoUnicoRepository.findByCodigo(codigoIngresso);

        if (ingressoOpt.isEmpty()) {
            return "Ingresso inválido! Código não encontrado.";
        }

        IngressoUnico ingresso = ingressoOpt.get();

        if (ingresso.isUtilizado()) {
            return "Ingresso já foi utilizado em " + ingresso.getDataUso();
        }

        // Marca o ingresso como utilizado
        ingresso.setUtilizado(true);
        ingresso.setDataUso(LocalDateTime.now());
        ingressoUnicoRepository.save(ingresso);

        return "Ingresso validado com sucesso!";
    }


    public String verificarStatusIngresso(Long ingressoId) {
        // Verifica se o ingresso existe
        Ingresso ingresso = ingressoRepository.findById(ingressoId).orElse(null);
        if (ingresso == null) {
            return "Ingresso não encontrado.";
        }

        return "Status do ingresso: " + ingresso.getStatus();
    }


    private void enviarEmailConfirmacaoCompra(String destinatario, String ingressoNome, int quantidade) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("Confirmação de Compra de Ingressos");
        mensagem.setText("Sua compra de " + quantidade + " ingresso(s) para " + ingressoNome + " foi efetuada com sucesso.");
        mailSender.send(mensagem);
    }


    public void confirmarPagamento(Long compraId) {
        Compra compra = compraRepository.findById(compraId).orElse(null);
        if (compra != null) {
            enviarEmailConfirmacaoPagamento(compra.getCompradorEmail(), compra.getIngresso().getEvento().getNome());
        }
    }


    private void enviarEmailConfirmacaoPagamento(String destinatario, String ingressoNome) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject("Pagamento Confirmado");
        mensagem.setText("O pagamento para o ingresso " + ingressoNome + " foi confirmado.");
        mailSender.send(mensagem);
    }

    public List<Compra> consultarComprasPorEmail(String email) {
        return compraRepository.findByCompradorEmail(email);
    }

    public List<Compra> consultarComprasFuturasPorEmail(String email) {
        LocalDate agora = LocalDate.now();
        return compraRepository.findByCompradorEmail(email).stream()
                .filter(compra -> compra.getIngresso().getEvento().getDatas().get(0).isAfter(agora))
                .collect(Collectors.toList());
    }
}
