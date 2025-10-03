package com.icecreamparlor.producao.controller;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.repositories.ProducaoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/producao")
public class ProducaoController {

    private final ProducaoRepository repository;

    public ProducaoController(ProducaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ProducaoEntity> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{pedidoId}")
    public ProducaoEntity buscarPorPedidoId(@PathVariable UUID pedidoId) {
        return repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Produção não encontrada para o pedido: " + pedidoId));
    }
}

