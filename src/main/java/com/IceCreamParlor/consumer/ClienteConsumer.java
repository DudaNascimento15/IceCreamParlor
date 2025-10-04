package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.service.ClienteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ClienteConsumer {

    private final ClienteServiceImpl clienteService;

    public ClienteConsumer(ClienteServiceImpl clienteService) {
        this.clienteService = clienteService;
    }

    @RabbitListener(queues = "q.cliente", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message, @Payload Object evento, @Headers Map<String, Object> headers) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if ("cliente.pedido.pronto".equals(routingKey) && evento instanceof ClienteEvents.PedidoPronto) {
            onPedidoPronto((ClienteEvents.PedidoPronto) evento);
        } else if ("cliente.pedido.despachado".equals(routingKey) && evento instanceof ClienteEvents.PedidoDespachado) {
            onPedidoDespachado((ClienteEvents.PedidoDespachado) evento);
        } else if ("cliente.pedido.a_caminho".equals(routingKey) && evento instanceof ClienteEvents.PedidoACaminho) {
            onPedidoACaminho((ClienteEvents.PedidoACaminho) evento);
        } else if ("cliente.pedido.entregue".equals(routingKey) && evento instanceof ClienteEvents.PedidoEntregue) {
            onPedidoEntregue((ClienteEvents.PedidoEntregue) evento);
        }
    }

    public void onPedidoPronto(ClienteEvents.PedidoPronto evento) {
        try {
            log.info("Recebida notificação de pedido pronto para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoPronto(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido pronto para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido pronto", e);
        }
    }

    public void onPedidoDespachado(ClienteEvents.PedidoDespachado evento) {
        try {
            log.info("Recebida notificação de pedido despachado para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoDespachado(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido despachado para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido despachado", e);
        }
    }

    public void onPedidoACaminho(ClienteEvents.PedidoACaminho evento) {
        try {
            log.info("Recebida notificação de pedido a caminho para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoACaminho(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido a caminho para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido a caminho", e);
        }
    }

    public void onPedidoEntregue(ClienteEvents.PedidoEntregue evento) {
        try {
            log.info("Recebida notificação de pedido entregue para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoEntregue(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido entregue para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido entregue", e);
        }
    }

    @RabbitListener(queues = "q.cliente.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleClienteDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("Mensagem em DLQ de Cliente - Routing Key: {}, Evento: {}, Razão: {}",
            routingKey, evento, reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}