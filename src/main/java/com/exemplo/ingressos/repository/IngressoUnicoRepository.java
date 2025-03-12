package com.exemplo.ingressos.repository;

import com.exemplo.ingressos.model.IngressoUnico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IngressoUnicoRepository extends JpaRepository<IngressoUnico, Long> {
    Optional<IngressoUnico> findByCodigo(String codigo);
}
