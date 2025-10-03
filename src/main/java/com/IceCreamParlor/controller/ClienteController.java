package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.ClienteEntity;
import com.IceCreamParlor.dto.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @GetMapping
    public List<ClienteEntity> listarTodas() {
        return clienteRepository.findAll();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<ClienteEntity> buscarPorPedido(@PathVariable UUID pedidoId) {
        return clienteRepository.findByPedidoId(pedidoId);
    }

    @GetMapping("/pedido/{clienteId}")
    public List<ClienteEntity> buscarPorCliente(@PathVariable String clienteId) {
        return clienteRepository.findByClienteId(clienteId);
    }

}
