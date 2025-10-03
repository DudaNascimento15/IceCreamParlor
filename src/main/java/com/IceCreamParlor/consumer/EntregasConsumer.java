package com.IceCreamParlor.consumer;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Envelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntregasConsumer {

    private final EntregaService entregaService;
    private final ProcessedEventRepository processed;
    private final MessageBus bus;

    @RabbitListener(queues = "q.entregas")
    public void onEntrega(Envelope<EntregaCriadaEvt> env) {
        if (processed.existsByMessageId(env.messageId())) return;
        try {
            String pedidoId = env.data().pedidoId();

            // 1. Cria a entrega no sistema
            entregaService.criarEntrega(pedidoId);

            // 2. Publica "despachado"
            bus.publish("entregas.pedido.despachado",
                new EntregaDespachadaEvt(pedidoId),
                env.correlationId(), env.usuario());

            // 3. Simula progresso da entrega
            entregaService.atualizarStatus(pedidoId, "a_caminho");
            bus.publish("entregas.pedido.a_caminho",
                new EntregaACaminhoEvt(pedidoId),
                env.correlationId(), env.usuario());

            // 4. Simula entrega finalizada
            entregaService.atualizarStatus(pedidoId, "entregue");
            bus.publish("entregas.pedido.entregue",
                new EntregaEntregueEvt(pedidoId),
                env.correlationId(), env.usuario());

            processed.save(new ProcessedEvent(env.messageId()));
        } catch (ReprocessavelException e) {
            // vai para DLX -> q.entregas.retry
            throw new AmqpRejectAndDontRequeueException("retry", e);
        } catch (Exception e) {
            // envenenada → q.entregas.dlq
            throw new ImmediateAcknowledgeAmqpException("poison", e);
        }
    }
}
