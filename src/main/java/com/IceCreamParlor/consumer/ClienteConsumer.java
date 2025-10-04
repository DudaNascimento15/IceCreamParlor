package com.IceCreamParlor.consumer;

import org.springframework.stereotype.Service;

import com.IceCreamParlor.service.ClienteServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ClienteConsumer {

    private final ClienteServiceImpl clienteService;

    public ClienteConsumer(ClienteServiceImpl clienteService) {
        this.clienteService = clienteService;
    }



   /* @RabbitListener(queues = "q.cliente")
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
    }*/
}
