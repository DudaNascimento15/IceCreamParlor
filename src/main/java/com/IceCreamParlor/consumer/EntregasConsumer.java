package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.service.EntregasServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EntregasConsumer {

    private final EntregasServiceImpl entregasService;
    private final ObjectMapper objectMapper;

    public EntregasConsumer(EntregasServiceImpl entregasService, ObjectMapper objectMapper) {
        this.entregasService = entregasService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.entregas", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String usuario = (String) message.getMessageProperties().getHeaders().getOrDefault("x-user", "system");

            log.info("🚚 EntregasConsumer - routingKey: {}", routingKey);

            if ("entregas.pedido.criar".equals(routingKey)) {
                WorkflowEvents.EntregaCriada evento = objectMapper.readValue(
                    message.getBody(),
                    WorkflowEvents.EntregaCriada.class
                );

                log.info("📦 Criando entrega para pedido: {}", evento.pedidoId());
                entregasService.processarEntrega(evento, evento.pedidoId().toString(), usuario);
            }
        } catch (Exception e) {
            log.error("❌ Erro no EntregasConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.entregas.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Entregas: {}", new String(message.getBody()));
    }
}