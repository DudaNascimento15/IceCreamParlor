package com.IceCreamParlor.controller;

import com.IceCreamParlor.service.insterfaces.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
class WorkflowController {

    private final WorkflowService relatorioService;

    @PostMapping("/pedidos")
    Map<String, Object> iniciar (@RequestParam String clienteId,
                                 @RequestParam BigDecimal valorTotal,
                                 @RequestHeader(name = "x-user") String user) {

        UUID id = relatorioService.iniciarPedido(clienteId, valorTotal, user);

        return Map.of("pedidoId", id.toString(), "status", "iniciado");
    }

}
