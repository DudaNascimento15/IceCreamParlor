package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.CaixaEntity;
import com.IceCreamParlor.dto.repositories.CaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaRepository caixaRepository;

    @GetMapping
    public List<CaixaEntity> listarTodas() {
        return caixaRepository.findAll();
    }

    @GetMapping("/por-pedido/{pedidoId}")
    public CaixaEntity buscarPorPedido(@PathVariable UUID pedidoId) {
        return caixaRepository.findById(pedidoId).orElse(null);
    }
}