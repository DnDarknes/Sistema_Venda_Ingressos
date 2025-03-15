package com.exemplo.ingressos.controller;

import com.exemplo.ingressos.model.Evento;
import com.exemplo.ingressos.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;


    @PostMapping
    public Evento criarEvento(@RequestBody Evento evento) {
        return eventoService.criarEvento(evento);
    }


    @GetMapping("/verificar-conflito")
    public boolean verificarConflito(@RequestParam("data") String data, @RequestParam("local") String local) {
        return eventoService.verificarConflitoDeHorario(LocalDate.parse(data), local);
    }


    @GetMapping
    public List<Evento> listarEventos() {
        return eventoService.listarTodos();
    }

    @GetMapping("/categoria/{categoria}")
    public List<Evento> buscarPorCategoria(@PathVariable String categoria) {
        return eventoService.buscarPorCategoria(categoria);
    }


    @GetMapping("/categoria/{categoria}/local/{local}")
    public List<Evento> buscarPorCategoriaELocal(@PathVariable String categoria, @PathVariable String local) {
        return eventoService.buscarPorCategoriaELocal(categoria, local);
    }


    @GetMapping("/categoria/{categoria}/local/{local}/data/{data}")
    public List<Evento> buscarPorTodosOsFiltros(
            @PathVariable String categoria,
            @PathVariable String local,
            @PathVariable String data) {

        LocalDate dataConvertida = LocalDate.parse(data);
        return eventoService.buscarPorCategoriaLocalEData(categoria, local, dataConvertida);
    }
}
