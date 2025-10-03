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
public class RelatorioConsumer {

    private final RelatorioService relatorioService;
    private final ProcessedEventRepository processed;

    @RabbitListener(queues = "q.relatorio")
    public void onRelatorio(Envelope<RelatorioEvt> env) {
        if (processed.existsByMessageId(env.messageId())) return;
        try {
            relatorioService.gerar(env.data()); // processa/gera relatório
            processed.save(new ProcessedEvent(env.messageId()));
        } catch (ReprocessavelException e) {
            throw new AmqpRejectAndDontRequeueException("retry", e);
        } catch (Exception e) {
            throw new ImmediateAcknowledgeAmqpException("poison", e);
        }
    }
}
