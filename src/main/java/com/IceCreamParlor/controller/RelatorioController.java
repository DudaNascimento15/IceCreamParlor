package com.IceCreamParlor.controller;


import com.IceCreamParlor.dto.entities.RelatorioEntity;
import com.IceCreamParlor.dto.repositories.RelatorioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioRepository repository;

    public RelatorioController(RelatorioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RelatorioEntity> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public RelatorioEntity buscarPorId(@PathVariable UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Relatório não encontrado com ID: " + id));
    }
}

