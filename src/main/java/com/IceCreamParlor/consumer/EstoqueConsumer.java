package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.service.EstoqueServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EstoqueConsumer {

    private final EstoqueServiceImpl estoqueService;
    private final ObjectMapper objectMapper;

    public EstoqueConsumer(EstoqueServiceImpl estoqueService, ObjectMapper objectMapper) {
        this.estoqueService = estoqueService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.estoque", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String usuario = (String) message.getMessageProperties().getHeaders().getOrDefault("x-user", "system");

            log.info("📦 EstoqueConsumer - routingKey: {}", routingKey);

            if ("estoque.reserva.solicitada".equals(routingKey)) {
                WorkflowEvents.ReservaSolicitada evento = objectMapper.readValue(
                    message.getBody(),
                    WorkflowEvents.ReservaSolicitada.class
                );

                log.info("📋 Processando reserva para pedido: {}", evento.pedidoId());
                estoqueService.processarReserva(evento, evento.pedidoId().toString(), usuario);
            }
        } catch (Exception e) {
            log.error("❌ Erro no EstoqueConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.estoque.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Estoque: {}", new String(message.getBody()));
    }
}