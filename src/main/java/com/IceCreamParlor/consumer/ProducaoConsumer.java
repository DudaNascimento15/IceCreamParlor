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
public class ProducaoConsumer {

    private final PedidoService pedidoService;
    private final ProcessedEventRepository processed;
    private final MessageBus bus;

    @RabbitListener(queues = "q.producao")
    public void onProducao(Envelope<PedidoConfirmadoEvt> env) {
        if (processed.existsByMessageId(env.messageId())) return;
        try {
            // Simula produção (fabricação/preparo do pedido)
            pedidoService.iniciarProducao(env.data().pedidoId());

            // Quando finalizado:
            pedidoService.finalizarProducao(env.data().pedidoId());
            bus.publish("atendimento.pedido.pronto",
                new PedidoProntoEvt(env.data().pedidoId()),
                env.correlationId(), env.usuario());

            processed.save(new ProcessedEvent(env.messageId()));
        } catch (ReprocessavelException e) {
            throw new AmqpRejectAndDontRequeueException("retry", e);
        } catch (Exception e) {
            throw new ImmediateAcknowledgeAmqpException("poison", e);
        }
    }
}
