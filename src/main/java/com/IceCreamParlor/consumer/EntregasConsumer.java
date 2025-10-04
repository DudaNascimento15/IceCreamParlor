package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.service.EntregasServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EntregasConsumer {

    private final EntregasServiceImpl entregasService;

    public EntregasConsumer(EntregasServiceImpl entregasService) {
        this.entregasService = entregasService;
    }

    @RabbitListener(queues = "q.entregas", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message, @Payload Object evento, @Headers Map<String, Object> headers) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if ("entregas.pedido.criar".equals(routingKey) && evento instanceof EntregaEvents.CriarEntrega) {
            onCriarEntrega((EntregaEvents.CriarEntrega) evento);
        } else if ("entregas.pedido.despachado".equals(routingKey) && evento instanceof EntregaEvents.PedidoDespachado) {
            onPedidoDespachado((EntregaEvents.PedidoDespachado) evento);
        } else if ("entregas.pedido.a_caminho".equals(routingKey) && evento instanceof EntregaEvents.PedidoACaminho) {
            onPedidoACaminho((EntregaEvents.PedidoACaminho) evento);
        } else if ("entregas.pedido.entregue".equals(routingKey) && evento instanceof EntregaEvents.PedidoEntregue) {
            onPedidoEntregue((EntregaEvents.PedidoEntregue) evento);
        }
    }

    public void onCriarEntrega(EntregaEvents.CriarEntrega evento) {
        log.info("Recebida solicitação de criação de entrega: pedidoId={}", evento.pedidoId());
        entregasService.criarEntrega(evento.pedidoId(), evento.clienteId());
        entregasService.processarEntrega(evento, evento.pedidoId().toString(), evento.clienteId());
    }

    public void onPedidoDespachado(EntregaEvents.PedidoDespachado evento) {
        log.info("Recebido pedido despachado: pedidoId={}", evento.pedidoId());
    }

    public void onPedidoACaminho(EntregaEvents.PedidoACaminho evento) {
        log.info("Recebido pedido a caminho: pedidoId={}", evento.pedidoId());
    }

    public void onPedidoEntregue(EntregaEvents.PedidoEntregue evento) {
        log.info("Recebido pedido entregue: pedidoId={}", evento.pedidoId());
    }

    @RabbitListener(queues = "q.entregas.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleEntregasDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("Mensagem em DLQ de Entregas - Routing Key: {}, Evento: {}, Razão: {}",
            routingKey, evento, reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}