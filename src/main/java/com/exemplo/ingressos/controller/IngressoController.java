package com.exemplo.ingressos.controller;

import com.exemplo.ingressos.model.Ingresso;
import com.exemplo.ingressos.service.IngressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingressos")
public class IngressoController {

    @Autowired
    private IngressoService ingressoService;

    @PostMapping
    public Ingresso criarIngresso(@RequestBody Ingresso ingresso) {
        return ingressoService.criarIngresso(ingresso);
    }

    @PutMapping("/{id}")
    public Ingresso atualizarIngresso(@PathVariable Long id, @RequestBody Ingresso ingresso) {
        return ingressoService.atualizarIngresso(id, ingresso);
    }
}
