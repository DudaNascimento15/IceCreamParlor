package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.service.EntregasServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitListener(queues = "q.entregas.pedido.criar")
    public void onCriarEntrega(EntregaEvents.CriarEntrega evento) {
        log.info("Recebida solicitação de criação de entrega: pedidoId={}", evento.pedidoId());
        entregasService.criarEntrega(evento.pedidoId(), evento.clienteId());
        entregasService.processarEntrega(evento, evento.pedidoId().toString(), evento.clienteId());
    }

    @RabbitListener(queues = "q.entregas.pedido.despachado")
    public void onPedidoDespachado(EntregaEvents.PedidoDespachado evento) {
        log.info("Recebido pedido despachado: pedidoId={}", evento.pedidoId());
    // Implementar ação se necessário
    }

    @RabbitListener(queues = "q.entregas.pedido.a_caminho")
    public void onPedidoACaminho(EntregaEvents.PedidoACaminho evento) {
        log.info("Recebido pedido a caminho: pedidoId={}", evento.pedidoId());
        // Implementar ação se necessário
    }

    @RabbitListener(queues = "q.entregas.pedido.entregue")
    public void onPedidoEntregue(EntregaEvents.PedidoEntregue evento) {
        log.info("Recebido pedido entregue: pedidoId={}", evento.pedidoId()); }
        // Implementar ação se necessário 

    @RabbitListener(queues = {"q.entregas.pedido.criar.dlq", "q.entregas.pedido.despachado.dlq", "q.entregas.pedido.a_caminho.dlq", "q.entregas.pedido.entregue.dlq"}, containerFactory = "rabbitListenerContainerFactory")
    public void handleEntregasDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        log.warn("Mensagem em DLQ de Entregas - Evento: {}, Razão: {} (TTL expired? {})", evento, reason, "expired".equals(reason));
        // Ação: entregasService.salvarParaAuditoria(evento, reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}