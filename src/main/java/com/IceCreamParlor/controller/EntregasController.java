package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.repositories.EntregaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregasController {

    private final EntregaRepository repository;

    @GetMapping
    public List<EntregaEntity> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{pedidoId}")
    public EntregaEntity buscarPorPedidoId(@PathVariable UUID pedidoId) {
        return repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada para o pedido: " + pedidoId));
    }
}

