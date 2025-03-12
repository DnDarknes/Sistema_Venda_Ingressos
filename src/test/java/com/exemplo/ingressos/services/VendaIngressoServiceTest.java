package com.exemplo.ingressos.services;

import com.exemplo.ingressos.VendaIngressosApplication;
import com.exemplo.ingressos.model.Compra;
import com.exemplo.ingressos.model.Evento;
import com.exemplo.ingressos.model.Ingresso;
import com.exemplo.ingressos.repository.CompraRepository;
import com.exemplo.ingressos.repository.EventoRepository;
import com.exemplo.ingressos.repository.IngressoRepository;
import com.exemplo.ingressos.service.VendaIngressoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendaIngressosApplication.class)
@ExtendWith(MockitoExtension.class)
class VendaIngressoServiceTest {

    @Mock
    private IngressoRepository ingressoRepository;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private VendaIngressoService vendaIngressoService;

    private Ingresso ingresso;

    @Autowired
    private EventoRepository eventoRepository;

    @BeforeEach
    void setUp() {
        // Criando e salvando um evento antes do ingresso
        Evento evento = new Evento();
        evento.setNome("Show do Rock");
        evento.setCapacidade(50);
        evento.setCategoria("musica");
        evento.setDescricao("um show de rock");
        evento.setLocal("cajazeiras");
        evento = eventoRepository.save(evento); // Salva o evento antes de associar

        // Criando o ingresso associado ao evento
        ingresso = new Ingresso();
        ingresso.setId(1L);
        ingresso.setQuantidade(100);  // Definindo a quantidade de ingressos
        ingresso.setEvento(evento);
        ingresso.setStatus("disponível");
        ingresso.setPreco(100.0);  // Inicializando o preco para evitar null
    }


    @Test
    void deveRealizarCompraQuandoIngressoEstaDisponivel() {
        // Configurando o mock do repositório de ingressos para retornar um ingresso existente
        when(ingressoRepository.findById(1L)).thenReturn(Optional.of(ingresso));

        // Chamando o método de compra com os parâmetros esperados
        String resultado = vendaIngressoService.comprarIngresso(1L, "João", "joao@email.com", 2);

        // Verificando se o resultado é o esperado
        assertEquals("Compra realizada com sucesso!", resultado);

        // Verificando se a quantidade de ingressos foi diminuída corretamente
        assertEquals(98, ingresso.getQuantidade()); // Deve diminuir a quantidade de 100 para 98

        // Verificando se o repositório de compras foi chamado uma vez para salvar a compra
        verify(compraRepository, times(1)).save(any(Compra.class));

        // Verificando se o e-mail foi enviado uma vez
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }


    @Test
    void deveRetornarErroQuandoIngressoNaoExiste() {
        when(ingressoRepository.findById(99L)).thenReturn(Optional.empty());

        String resultado = vendaIngressoService.comprarIngresso(99L, "João", "joao@email.com", 2);

        assertEquals("Ingresso não encontrado.", resultado);
    }

    @Test
    void deveRetornarErroQuandoQuantidadeSolicitadaEhMaiorQueDisponivel() {
        ingresso.setQuantidade(1);
        when(ingressoRepository.findById(1L)).thenReturn(Optional.of(ingresso));

        String resultado = vendaIngressoService.comprarIngresso(1L, "João", "joao@email.com", 2);

        assertEquals("Quantidade de ingressos solicitada maior que a disponível.", resultado);
    }

    @Test
    void deveRetornarStatusDoIngresso() {
        when(ingressoRepository.findById(1L)).thenReturn(Optional.of(ingresso));

        String resultado = vendaIngressoService.verificarStatusIngresso(1L);

        assertEquals("Status do ingresso: disponível", resultado);
    }

    @Test
    void deveConfirmarPagamentoQuandoCompraExiste() {
        Compra compra = new Compra();
        compra.setCompradorEmail("joao@email.com");
        compra.setIngresso(ingresso);

        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));

        vendaIngressoService.confirmarPagamento(1L);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void naoDeveConfirmarPagamentoQuandoCompraNaoExiste() {
        when(compraRepository.findById(1L)).thenReturn(Optional.empty());

        vendaIngressoService.confirmarPagamento(1L);

        verify(mailSender, never()).send(any(SimpleMailMessage.class));// Não deve enviar e-mail se a compra não existir
    }
}
