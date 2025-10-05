package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.service.CaixaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaixaConsumer {

    private final CaixaServiceImpl caixaService;
    private final ObjectMapper objectMapper;

    public CaixaConsumer(CaixaServiceImpl caixaService, ObjectMapper objectMapper) {
        this.caixaService = caixaService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.caixa", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String usuario = (String) message.getMessageProperties().getHeaders().getOrDefault("x-user", "system");

            log.info("💳 CaixaConsumer - routingKey: {}", routingKey);

            if ("caixa.pagamento.iniciado".equals(routingKey)) {
                WorkflowEvents.PagamentoIniciado evento = objectMapper.readValue(
                    message.getBody(),
                    WorkflowEvents.PagamentoIniciado.class
                );

                log.info("💰 Processando pagamento para pedido: {}", evento.pedidoId());
                caixaService.processarPagamento(evento, evento.pedidoId().toString(), usuario);
            }
        } catch (Exception e) {
            log.error("❌ Erro no CaixaConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.caixa.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Caixa: {}", new String(message.getBody()));
    }
}