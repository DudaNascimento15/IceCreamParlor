package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.repositories.CaixaRepository;
import com.IceCreamParlor.service.CaixaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaRepository caixaRepository;
    private final CaixaServiceImpl caixaService;


    @GetMapping("/pagamentos")
    public Object listarPagamentos() {
        return caixaRepository.findAll();
    }

    @GetMapping("/pagamentos/{pedidoId}")
    public Object buscarPorPedidoId(@PathVariable String pedidoId) {
        return caixaRepository.findAll();
    }

    @PostMapping("pagamentos")
    public void simularPagamento(  @RequestParam UUID pedidoId,
                                   @RequestParam String clienteId,
                                   @RequestParam BigDecimal valorTotal) {
       // CaixaEvents evento = new CaixaEvents(pedidoId, clienteId, valorTotal);
       // caixaService.processarPagamento(evento);
    }




}
