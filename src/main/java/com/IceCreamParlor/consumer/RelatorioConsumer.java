package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.RelatorioEvents;
import com.IceCreamParlor.service.RelatorioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RelatorioConsumer {

    private final RelatorioServiceImpl relatorioService;
    private final ObjectMapper objectMapper;

    public RelatorioConsumer(RelatorioServiceImpl relatorioService, ObjectMapper objectMapper) {
        this.relatorioService = relatorioService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.relatorio", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();

            log.info("📊 RelatorioConsumer - routingKey: {}", routingKey);

            if ("relatorio.evento.recebido".equals(routingKey)) {
                RelatorioEvents.EventoRecebido evento = objectMapper.readValue(
                    message.getBody(),
                    RelatorioEvents.EventoRecebido.class
                );

                log.info("📝 Salvando evento no relatório: {}", evento.nomeEvento());
                relatorioService.salvarEvento(evento.nomeEvento(), evento.conteudo());
            }
        } catch (Exception e) {
            log.error("❌ Erro no RelatorioConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.relatorio.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Relatorio: {}", new String(message.getBody()));
    }
}