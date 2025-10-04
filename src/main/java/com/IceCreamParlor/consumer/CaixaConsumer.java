package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.service.CaixaServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CaixaConsumer {

    private final CaixaServiceImpl caixaService;

    public CaixaConsumer(CaixaServiceImpl caixaService) {
        this.caixaService = caixaService;
    }

    @RabbitListener(queues = "q.caixa", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message, @Payload Object evento, @Headers Map<String, Object> headers) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if ("caixa.pagamento.iniciado".equals(routingKey) && evento instanceof CaixaEvents.PagamentoIniciado) {
            onPagamentoIniciado((CaixaEvents.PagamentoIniciado) evento);
        } else if ("caixa.pagamento.aprovado".equals(routingKey) && evento instanceof CaixaEvents.PagamentoAprovado) {
            onPagamentoAprovado((CaixaEvents.PagamentoAprovado) evento);
        } else if ("caixa.pagamento.negado".equals(routingKey) && evento instanceof CaixaEvents.PagamentoNegado) {
            onPagamentoNegado((CaixaEvents.PagamentoNegado) evento);
        }
    }

    public void onPagamentoIniciado(CaixaEvents.PagamentoIniciado evento) {
        try {
            log.info("Pagamento iniciado para pedido: {}", evento.pedidoId());
            // Ajuste para usar os campos disponíveis
            caixaService.processarPagamento(
                    new WorkflowEvents.PagamentoIniciado(evento.pedidoId(), evento.clientId(), BigDecimal.ZERO),
                    evento.pedidoId().toString(), evento.clientId());
        } catch (Exception e) {
            log.error("Erro processando pagamento iniciado para pedido {}: {}", evento.pedidoId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha no processamento de pagamento iniciado", e);
        }
    }

    public void onPagamentoAprovado(CaixaEvents.PagamentoAprovado evento) {
        try {
            log.info("Pagamento aprovado para pedido: {}", evento.pedidoId());
            caixaService.processarPagamento(
                    new WorkflowEvents.PagamentoIniciado(evento.pedidoId(), evento.clientId(), evento.valor()),
                    evento.pedidoId().toString(), evento.clientId());
        } catch (Exception e) {
            log.error("Erro processando pagamento aprovado para pedido {}: {}", evento.pedidoId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha no processamento de pagamento aprovado", e);
        }
    }

    public void onPagamentoNegado(CaixaEvents.PagamentoNegado evento) {
        try {
            log.info("Pagamento negado para pedido: {}", evento.pedidoId());
        } catch (Exception e) {
            log.error("Erro processando pagamento negado para pedido {}: {}", evento.pedidoId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha no processamento de pagamento negado", e);
        }
    }

    @RabbitListener(queues = { "q.caixa.pagamento.iniciado.dlq", "q.caixa.pagamento.aprovado.dlq",
            "q.caixa.pagamento.negado.dlq" }, containerFactory = "rabbitListenerContainerFactory")
    public void handleCaixaDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        log.warn("Mensagem em DLQ de Caixa - Evento: {}, Razão: {} (TTL expired? {})", evento, reason,
                "expired".equals(reason));
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders()
                .get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}