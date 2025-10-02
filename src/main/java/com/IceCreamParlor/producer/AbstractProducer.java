package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public abstract class AbstractProducer {

    protected final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "sorv.ex";

    protected AbstractProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    protected void publish(String routingKey, Object payload, String correlationId, String usuario) {
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, payload, message -> {
            message.getMessageProperties().setHeader("correlationId", correlationId);
            message.getMessageProperties().setHeader("usuario", usuario);
            return message;
        });
    }
}
