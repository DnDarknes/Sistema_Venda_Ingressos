package com.exemplo.ingressos.repository;

import com.exemplo.ingressos.model.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngressoRepository extends JpaRepository<Ingresso, Long> {
}
