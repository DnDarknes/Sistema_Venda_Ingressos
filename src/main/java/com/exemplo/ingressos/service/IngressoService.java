package com.exemplo.ingressos.service;

import com.exemplo.ingressos.model.Ingresso;
import com.exemplo.ingressos.repository.IngressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngressoService {

    @Autowired
    private IngressoRepository ingressoRepository;

    public Ingresso criarIngresso(Ingresso ingresso) {

        return ingressoRepository.save(ingresso);
    }

    public Ingresso atualizarIngresso(Long id, Ingresso ingresso) {
        if (!ingressoRepository.existsById(id)) {
            throw new RuntimeException("Ingresso não encontrado!");
        }
        ingresso.setId(id);
        return ingressoRepository.save(ingresso);
    }
}
