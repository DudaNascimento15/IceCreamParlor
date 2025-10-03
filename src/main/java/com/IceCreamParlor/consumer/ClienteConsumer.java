package com.IceCreamParlor.consumer;

import com.IceCreamParlor.service.insterfaces.ClienteService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.IceCreamParlor.service.ClienteService;
import com.rabbitmq.client.Envelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ClienteConsumer {

    private final ClienteService clienteService;

    public ClienteConsumer(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    private final  processed;

    @RabbitListener(queues = "q.cliente")
    public void onCliente(Envelope<ClienteEvt> env) {
        if (processed.existsByMessageId(env.messageId())) return;
        try {
            // Exemplo: notificação de status do pedido
            clienteService.notificar(env.data());

            processed.save(new ProcessedEvent(env.messageId()));
        } catch (ReprocessavelException e) {
            throw new AmqpRejectAndDontRequeueException("retry", e);
        } catch (Exception e) {
            throw new ImmediateAcknowledgeAmqpException("poison", e);
        }
    }
}
