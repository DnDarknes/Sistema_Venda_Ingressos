package com.exemplo.ingressos.repository;

import com.exemplo.ingressos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByLocalAndDatasContaining(String local, LocalDate data);

    // Filtrar eventos por categoria
    List<Evento> findByCategoria(String categoria);

    // Filtrar eventos por categoria e local
    List<Evento> findByCategoriaAndLocal(String categoria, String local);

    // Filtrar eventos por categoria, local e data específica
    List<Evento> findByCategoriaAndLocalAndDatasContaining(String categoria, String local, LocalDate data);
}
