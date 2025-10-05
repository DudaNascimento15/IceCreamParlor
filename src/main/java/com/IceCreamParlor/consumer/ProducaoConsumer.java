package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.service.ProducaoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProducaoConsumer {

    private final ProducaoServiceImpl producaoService;
    private final ObjectMapper objectMapper;

    public ProducaoConsumer(ProducaoServiceImpl producaoService, ObjectMapper objectMapper) {
        this.producaoService = producaoService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.producao", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String usuario = (String) message.getMessageProperties().getHeaders().getOrDefault("x-user", "system");

            log.info("🏭 ProducaoConsumer - routingKey: {}", routingKey);

            if ("pedidos.pedido.confirmado".equals(routingKey)) {
                WorkflowEvents.PedidoConfirmado evento = objectMapper.readValue(
                    message.getBody(),
                    WorkflowEvents.PedidoConfirmado.class
                );

                log.info("✅ Pedido confirmado para produção: {}", evento.pedidoId());
                producaoService.iniciarProducao(evento, evento.pedidoId().toString(), usuario);
            }
        } catch (Exception e) {
            log.error("❌ Erro no ProducaoConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.producao.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Producao: {}", new String(message.getBody()));
    }
}