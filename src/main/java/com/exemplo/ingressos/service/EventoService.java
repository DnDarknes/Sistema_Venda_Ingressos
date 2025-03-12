package com.exemplo.ingressos.service;

import com.exemplo.ingressos.model.Evento;
import com.exemplo.ingressos.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // Método para criar um evento validando a capacidade dele
    public Evento criarEvento(Evento evento) {
        if (evento.getCapacidade() <= 0) {
            throw new IllegalArgumentException("Capacidade do evento deve ser maior que 0.");
        }
        return eventoRepository.save(evento);
    }

    // Método para verificar conflito de horário e local
    public boolean verificarConflitoDeHorario(LocalDate data, String local) {
        List<Evento> eventosNoLocal = eventoRepository.findByLocalAndDatasContaining(local, data);
        return !eventosNoLocal.isEmpty();
    }

    // Método para verificar se a capacidade do evento é válida
    public boolean verificarCapacidade(Evento evento) {
        return evento.getCapacidade() > 0;
    }

    // Listar todos os eventos
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    // Buscar eventos por categoria
    public List<Evento> buscarPorCategoria(String categoria) {
        return eventoRepository.findByCategoria(categoria);
    }

    // Buscar eventos por categoria e local
    public List<Evento> buscarPorCategoriaELocal(String categoria, String local) {
        return eventoRepository.findByCategoriaAndLocal(categoria, local);
    }

    // Buscar eventos por categoria, local e data
    public List<Evento> buscarPorCategoriaLocalEData(String categoria, String local, LocalDate data) {
        return eventoRepository.findByCategoriaAndLocalAndDatasContaining(categoria, local, data);
    }
    // Buscar evento por ID (inserido)
    public Evento buscarEventoPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
    }
}
