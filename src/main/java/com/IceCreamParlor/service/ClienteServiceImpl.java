package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ClienteEntity;
import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.dto.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class ClienteServiceImpl {

  /*  private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void notificarPedidoPronto(ClienteEvents.PedidoPronto pedido) {
        salvarNotificacao(UUID.fromString(pedido.clienteId()), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " está pronto !");
    }

    public void notificarPedidoDespachado(ClienteEvents.PedidoDespachado pedido) {
        salvarNotificacao(UUID.fromString(pedido.clienteId()), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " foi despachado!");
    }

    public void notificarPedidoACaminho(ClienteEvents.PedidoACaminho pedido) {
        salvarNotificacao(UUID.fromString(pedido.clienteId()), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " está a caminho!");
    }

    public void notificarPedidoEntregue(ClienteEvents.PedidoEntregue pedido) {
        salvarNotificacao(UUID.fromString(pedido.clienteId()), pedido.clienteId(), "Seu pedido " + pedido.pedidoId() + " foi entregue, bom apetite!");
    }

    public void registrarNotificacao( cliente) {
        ClienteEntity notificacao = new NotificacaoEntity();
        notificacao.(cliente.getPedidoId());
        notificacao.setClienteId(cliente.getClienteId());
        notificacao.setMensagem(cliente.getMensagem());
        notificacao.setCriadoEm(OffsetDateTime.now());

        notificacaoRepository.save(notificacao);
    }

*/
}
