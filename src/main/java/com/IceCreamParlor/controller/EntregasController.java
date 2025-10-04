package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.repositories.EntregaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregasController {

    private final EntregaRepository entregaRepository;

    @GetMapping
    public List<EntregaEntity> listarTodas() {
        return entregaRepository.findAll();
    }

    @GetMapping("/por-pedido/{pedidoId}")
    public EntregaEntity buscarPorPedido(@PathVariable UUID pedidoId) {
        return entregaRepository.findById(pedidoId).orElse(null);
    }
}