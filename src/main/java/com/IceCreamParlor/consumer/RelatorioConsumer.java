package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.RelatorioEvents;
import com.IceCreamParlor.service.RelatorioServiceImpl;
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
public class RelatorioConsumer {

    private final RelatorioServiceImpl relatorioService;

    public RelatorioConsumer(RelatorioServiceImpl relatorioService) {
        this.relatorioService = relatorioService;
    }

    @RabbitListener(queues = "q.relatorio", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message, @Payload Object evento, @Headers Map<String, Object> headers) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if ("relatorio.evento.recebido".equals(routingKey) && evento instanceof RelatorioEvents.EventoRecebido) {
            onEventoRecebido((RelatorioEvents.EventoRecebido) evento);
        }
    }

    public void onEventoRecebido(RelatorioEvents.EventoRecebido evento) {
        try {
            log.info("Relatório: evento recebido - nome: {}", evento.nomeEvento());
            relatorioService.salvarEvento(evento.nomeEvento(), evento.conteudo());
        } catch (Exception e) {
            log.error("Erro salvando evento recebido {}: {}", evento.nomeEvento(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha no salvamento de evento recebido", e);
        }
    }

    @RabbitListener(queues = "q.relatorio.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleRelatorioDlq(RelatorioEvents.EventoRecebido evento, Message message) {
        String reason = extractDeathReason(message);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.warn("Mensagem em DLQ de Relatório - Routing Key: {}, Evento: {}, Razão: {}",
            routingKey, evento != null ? evento.nomeEvento() : "null", reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders()
            .get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}