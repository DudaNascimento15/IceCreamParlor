package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.service.ClienteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitListener(queues = "q.cliente.pedido.pronto", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoPronto(ClienteEvents.PedidoPronto evento) {
        try {
            log.info("Recebida notificação de pedido pronto para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoPronto(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido pronto para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido pronto", e);
        }
    }

    @RabbitListener(queues = "q.cliente.pedido.despachado", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoDespachado(ClienteEvents.PedidoDespachado evento) {
        try {
            log.info("Recebida notificação de pedido despachado para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoDespachado(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido despachado para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido despachado", e);
        }
    }

    @RabbitListener(queues = "q.cliente.pedido.a_caminho", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoACaminho(ClienteEvents.PedidoACaminho evento) {
        try {
            log.info("Recebida notificação de pedido a caminho para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoACaminho(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido a caminho para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido a caminho", e);
        }
    }

    @RabbitListener(queues = "q.cliente.pedido.entregue", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoEntregue(ClienteEvents.PedidoEntregue evento) {
        try {
            log.info("Recebida notificação de pedido entregue para cliente: {}", evento.clienteId());
            clienteService.notificarPedidoEntregue(evento);
        } catch (Exception e) {
            log.error("Erro notificando pedido entregue para cliente {}: {}", evento.clienteId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na notificação de pedido entregue", e);
        }
    }

    @RabbitListener(queues = {"q.cliente.pedido.pronto.dlq", "q.cliente.pedido.despachado.dlq", "q.cliente.pedido.a_caminho.dlq", "q.cliente.pedido.entregue.dlq"}, containerFactory = "rabbitListenerContainerFactory")
    public void handleClienteDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        log.warn("Mensagem em DLQ de Cliente - Evento: {}, Razão: {} (TTL expired? {})", evento, reason, "expired".equals(reason));
        // Ação: clienteService.salvarParaAuditoria(evento, reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}