package com.IceCreamParlor.messaging;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EventPublisher {

    private final RabbitTemplate template;

    public EventPublisher(RabbitTemplate template) { this.template = template; }

    public void publish(String routingKey, Object payload, Map<String, Object> headers) {
        MessagePostProcessor mpp = msg -> {
            if (headers != null) headers.forEach((k,v) -> msg.getMessageProperties().setHeader(k, v));
            msg.getMessageProperties().setContentType("application/json");
            return msg;
        };
        template.convertAndSend("sorv.ex", routingKey, payload, mpp);
    }

    public void publishRetry(String retryRoutingKey, Object payload) {
        template.convertAndSend("sorv.ex", retryRoutingKey, payload);
    }
}
