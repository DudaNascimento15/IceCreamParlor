package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ClienteEntity;
import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.dto.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ClienteServiceImpl {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public void notificarPedidoPronto(ClienteEvents.PedidoPronto pedido) {
        salvarNotificacao(pedido.pedidoId(), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " está pronto!");
    }

    @Transactional
    public void notificarPedidoDespachado(ClienteEvents.PedidoDespachado pedido) {
        salvarNotificacao(pedido.pedidoId(), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " foi despachado!");
    }

    @Transactional
    public void notificarPedidoACaminho(ClienteEvents.PedidoACaminho pedido) {
        salvarNotificacao(pedido.pedidoId(), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " está a caminho!");
    }

    @Transactional
    public void notificarPedidoEntregue(ClienteEvents.PedidoEntregue pedido) {
        salvarNotificacao(pedido.pedidoId(), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " foi entregue, bom apetite!");
    }

    private void salvarNotificacao(UUID pedidoId, String clienteId, String mensagem) {
        ClienteEntity notificacao = new ClienteEntity(pedidoId, clienteId, mensagem);
        clienteRepository.save(notificacao);
    }
}