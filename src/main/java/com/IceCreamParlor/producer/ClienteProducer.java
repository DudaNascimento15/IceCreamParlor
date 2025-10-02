package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClienteProducer extends AbstractProducer {

    public ClienteProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void publishNotificacao(PedidoDto pedido, String tipoEvento, String correlationId, String usuario) {
        publish("cliente." + tipoEvento, pedido, correlationId, usuario);
    }
}
