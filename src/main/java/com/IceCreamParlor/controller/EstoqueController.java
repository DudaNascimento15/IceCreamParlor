package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.EstoqueEntity;
import com.IceCreamParlor.dto.repositories.EstoqueRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueRepository repository;

    public EstoqueController(EstoqueRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<EstoqueEntity> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{pedidoId}")
    public EstoqueEntity buscarPorPedidoId(@PathVariable UUID pedidoId) {
        return repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + pedidoId));
    }
}
