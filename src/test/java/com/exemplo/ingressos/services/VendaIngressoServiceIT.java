package com.exemplo.ingressos.services;


import com.exemplo.ingressos.model.Evento;
import com.exemplo.ingressos.model.Ingresso;
import com.exemplo.ingressos.repository.CompraRepository;
import com.exemplo.ingressos.repository.EventoRepository;
import com.exemplo.ingressos.repository.IngressoRepository;
import com.exemplo.ingressos.service.VendaIngressoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VendaIngressoServiceIT {

    @Autowired
    private VendaIngressoService vendaIngressoService;

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private EventoRepository eventoRepository;

    private Ingresso ingresso;

    @BeforeEach
    void setUp() {
        // Limpa os dados do banco antes de cada teste
        ingressoRepository.deleteAll();
        eventoRepository.deleteAll();

        // Criando e salvando um evento antes do ingresso
        Evento evento = new Evento();
        evento.setNome("Show do Rock");
        evento.setCapacidade(50);
        evento.setCategoria("musica");
        evento.setDescricao("um show de rock");
        evento.setLocal("cajazeiras");

        List<LocalDate> datas = new ArrayList<>();
        datas.add(LocalDate.now().plusDays(1));  // Adicionando uma data válida
        evento.setDatas(datas);

        evento = eventoRepository.save(evento); // Salva o evento no banco

        // Criando um ingresso associado ao evento
        ingresso = new Ingresso();
        ingresso.setEvento(evento);
        ingresso.setQuantidade(10);
        ingresso.setStatus("disponível");
        ingresso.setPreco(50.0);
        ingresso.setData(LocalDate.now().plusDays(1));  // Definindo a data do ingresso
        ingresso.setRestricaoPublico("sem restrição");
        ingresso.setTipo("regular");  // Definindo o tipo do ingresso

        ingresso = ingressoRepository.saveAndFlush(ingresso);  // Salva o ingresso no banco
    }

    @Test
    void deveRealizarCompraComBancoReal() {
        // Verifica se o ID do ingresso foi gerado corretamente
        assertNotNull(ingresso.getId());

        String resultado = vendaIngressoService.comprarIngresso(ingresso.getId(), "Carlos", "carlos@email.com", 3);

        assertEquals("Compra realizada com sucesso!", resultado);

        // Verifica se a quantidade foi atualizada no banco
        Ingresso ingressoAtualizado = ingressoRepository.findById(ingresso.getId()).orElse(null);
        assertNotNull(ingressoAtualizado);
        assertEquals(7, ingressoAtualizado.getQuantidade()); // Deve ter diminuído para 7
    }

    @Test
    void naoDeveRealizarCompraComQuantidadeInsuficiente() {
        // Simulando um ingresso com quantidade insuficiente
        ingresso.setQuantidade(0);
        ingressoRepository.save(ingresso);

        // Tentativa de compra de mais ingressos do que disponíveis
        String resultado = vendaIngressoService.comprarIngresso(ingresso.getId(), "Ana", "ana@email.com", 1);

        assertEquals("Quantidade de ingressos solicitada maior que a disponível.", resultado);

        // Verifica que a quantidade de ingressos não foi alterada
        Ingresso ingressoAtualizado = ingressoRepository.findById(ingresso.getId()).orElse(null);
        assertNotNull(ingressoAtualizado);
        assertEquals(0, ingressoAtualizado.getQuantidade()); // A quantidade deve continuar 0
    }

    @Test
    void deveRetornarErroQuandoIngressoNaoExiste() {
        String resultado = vendaIngressoService.comprarIngresso(999L, "João", "joao@email.com", 2);

        assertEquals("Ingresso não encontrado.", resultado);
    }
}
