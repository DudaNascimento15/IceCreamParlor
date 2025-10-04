package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.service.ProducaoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProducaoConsumer {

    private final ProducaoServiceImpl producaoService;

    public ProducaoConsumer(ProducaoServiceImpl producaoService) {
        this.producaoService = producaoService;
    }

    @RabbitListener(queues = "q.producao.pedido.confirmado", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoConfirmado(WorkflowEvents.PedidoConfirmado evento) {
        try {
            log.info("Pedido confirmado para produção: {}", evento.pedidoId());
            producaoService.iniciarProducao(evento, evento.pedidoId().toString(), evento.clienteId());
        } catch (Exception e) {
            log.error("Erro iniciando produção para pedido {}: {}", evento.pedidoId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na iniciação de produção", e);
        }
    }

    @RabbitListener(queues = "q.producao.pedido.pronto", containerFactory = "rabbitListenerContainerFactory")
    public void onPedidoPronto(ProducaoEvents.PedidoPronto evento) {
        try {
            log.info("Pedido pronto para cliente: {}", evento.clienteId());
            producaoService.finalizarProducao(evento.pedidoId(), evento.clienteId());
        } catch (Exception e) {
            log.error("Erro finalizando produção para pedido {}: {}", evento.pedidoId(), e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Falha na finalização de produção", e);
        }
    }

    @RabbitListener(queues = {"q.producao.pedido.confirmado.dlq", "q.producao.pedido.pronto.dlq"}, containerFactory = "rabbitListenerContainerFactory")
    public void handleProducaoDlq(Object evento, Message message) {
        String reason = extractDeathReason(message);
        log.warn("Mensagem em DLQ de Produção - Evento: {}, Razão: {} (TTL expired? {})", evento, reason, "expired".equals(reason));
        // Ação: producaoService.salvarParaAuditoria(evento, reason);
    }

    private String extractDeathReason(Message message) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xDeath = (List<Map<String, Object>>) message.getMessageProperties().getHeaders().get("x-death");
        return xDeath != null && !xDeath.isEmpty() ? (String) xDeath.get(0).get("reason") : "unknown";
    }
}