package com.IceCreamParlor.messaging_rabbitmq;

import java.time.Instant;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MessagingRabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingRabbitmqApplication.class, args);
	}
	
    private final RabbitTemplate rabbitTemplate = new RabbitTemplate();
    private static final String EXCHANGE = "sorv.ex";

    public <T> void publish(String routingKey, T payload, String correlationId, String usuario) {
        var env = new Envelope<>(UUID.randomUUID().toString(), correlationId, usuario, Instant.now(), payload);
        rabbitTemplate.convertAndSend(EXCHANGE, routingKey, env, m -> {
            m.getMessageProperties().setCorrelationId(correlationId);
            return m;
        });
    }
}

