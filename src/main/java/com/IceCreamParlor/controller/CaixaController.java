package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.CaixaEntity;
import com.IceCreamParlor.repositories.CaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaRepository caixaRepository;

    @GetMapping
    public ResponseEntity<List<CaixaEntity>> listarTodas() {
        List<CaixaEntity> caixas = caixaRepository.findAll();
        return ResponseEntity.ok(caixas);
    }

    @GetMapping("/por-pedido/{pedidoId}")
    public ResponseEntity<CaixaEntity> buscarPorPedido(@PathVariable UUID pedidoId) {
        CaixaEntity caixa = caixaRepository.findByPedidoId(pedidoId).orElse(null);
        return ResponseEntity.ok(caixa);
    }
}