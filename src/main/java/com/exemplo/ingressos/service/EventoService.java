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


    public Evento criarEvento(Evento evento) {
        if (evento.getCapacidade() <= 0) {
            throw new IllegalArgumentException("Capacidade do evento deve ser maior que 0.");
        }
        return eventoRepository.save(evento);
    }


    public boolean verificarConflitoDeHorario(LocalDate data, String local) {
        List<Evento> eventosNoLocal = eventoRepository.findByLocalAndDatasContaining(local, data);
        return !eventosNoLocal.isEmpty();
    }


    public boolean verificarCapacidade(Evento evento) {
        return evento.getCapacidade() > 0;
    }


    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }


    public List<Evento> buscarPorCategoria(String categoria) {
        return eventoRepository.findByCategoria(categoria);
    }


    public List<Evento> buscarPorCategoriaELocal(String categoria, String local) {
        return eventoRepository.findByCategoriaAndLocal(categoria, local);
    }


    public List<Evento> buscarPorCategoriaLocalEData(String categoria, String local, LocalDate data) {
        return eventoRepository.findByCategoriaAndLocalAndDatasContaining(categoria, local, data);
    }

    public Evento buscarEventoPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
    }
}
