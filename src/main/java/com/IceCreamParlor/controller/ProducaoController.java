package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.repositories.ProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/producao")
@RequiredArgsConstructor
public class ProducaoController {

    private final ProducaoRepository producaoRepository;

    @GetMapping
    public List<ProducaoEntity> listarTodas() {
        return producaoRepository.findAll();
    }

    @GetMapping("/por-pedido/{pedidoId}")
    public ProducaoEntity buscarPorPedido(@PathVariable UUID pedidoId) {
        return producaoRepository.findById(pedidoId).orElse(null);
    }
}