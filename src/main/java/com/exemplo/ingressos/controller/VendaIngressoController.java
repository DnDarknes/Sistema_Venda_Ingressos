package com.exemplo.ingressos.controller;

import com.exemplo.ingressos.dto.CompraRequest;
import com.exemplo.ingressos.model.Compra;
import com.exemplo.ingressos.service.VendaIngressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaIngressoController {

    @Autowired
    private VendaIngressoService vendaIngressoService;

    public VendaIngressoController(VendaIngressoService vendaIngressoService) {
        this.vendaIngressoService = vendaIngressoService;
    }

    @PostMapping("/comprar")
    public ResponseEntity<String> comprarIngresso(@RequestBody CompraRequest compraRequest) {
        String resultado = vendaIngressoService.comprarIngresso(
                compraRequest.getIngressoId(),
                compraRequest.getCompradorNome(),
                compraRequest.getCompradorEmail(),
                compraRequest.getQuantidade()
        );
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/validar-ingresso")
    public ResponseEntity<String> validarIngresso(@RequestParam String codigoIngresso) {
        String resultado = vendaIngressoService.validarIngresso(codigoIngresso);
        return ResponseEntity.ok(resultado);
    }

    // Consultar todas as compras feitas por um usuário
    @GetMapping("/consultar-compras")
    public ResponseEntity<List<Compra>> consultarCompras(@RequestParam String email) {
        List<Compra> compras = vendaIngressoService.consultarComprasPorEmail(email);
        return ResponseEntity.ok(compras);
    }

    // Consultar apenas as compras para eventos futuros
    @GetMapping("/consultar-compras-futuras")
    public ResponseEntity<List<Compra>> consultarComprasFuturas(@RequestParam String email) {
        List<Compra> comprasFuturas = vendaIngressoService.consultarComprasFuturasPorEmail(email);
        return ResponseEntity.ok(comprasFuturas);
    }
}
