package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.dto.events.EstoqueEvents;
import com.IceCreamParlor.service.WorkflowServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkflowConsumer {

    private final WorkflowServiceImpl workflowService;
    private final ObjectMapper objectMapper;

    public WorkflowConsumer(WorkflowServiceImpl workflowService, ObjectMapper objectMapper) {
        this.workflowService = workflowService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.workflow", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String usuario = (String) message.getMessageProperties().getHeaders().getOrDefault("x-user", "system");

            log.info("⚙️ WorkflowConsumer - routingKey: {}", routingKey);

            if ("caixa.pagamento.aprovado".equals(routingKey)) {
                CaixaEvents.PagamentoAprovado evento = objectMapper.readValue(
                    message.getBody(),
                    CaixaEvents.PagamentoAprovado.class
                );
                log.info("💰 Pagamento aprovado - pedido: {}", evento.pedidoId());
                workflowService.pagamentoAprovado(evento.pedidoId(), usuario);

            } else if ("caixa.pagamento.negado".equals(routingKey)) {
                CaixaEvents.PagamentoNegado evento = objectMapper.readValue(
                    message.getBody(),
                    CaixaEvents.PagamentoNegado.class
                );
                log.info("❌ Pagamento negado - pedido: {}", evento.pedidoId());
                workflowService.pagamentoNegado(evento.pedidoId());

            } else if ("estoque.reserva.confirmada".equals(routingKey)) {
                EstoqueEvents.ReservaConfirmada evento = objectMapper.readValue(
                    message.getBody(),
                    EstoqueEvents.ReservaConfirmada.class
                );
                log.info("📦 Reserva confirmada - pedido: {}", evento.pedidoId());
                workflowService.reservaConfirmada(evento.pedidoId(), usuario);

            } else if ("estoque.reserva.negada".equals(routingKey)) {
                EstoqueEvents.ReservaNegada evento = objectMapper.readValue(
                    message.getBody(),
                    EstoqueEvents.ReservaNegada.class
                );
                log.info("❌ Reserva negada - pedido: {}", evento.pedidoId());
                workflowService.reservaNegada(evento.pedidoId());
            }
        } catch (Exception e) {
            log.error("❌ Erro no WorkflowConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.workflow.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Workflow: {}", new String(message.getBody()));
    }
}