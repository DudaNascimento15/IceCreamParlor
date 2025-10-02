package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducaoProducer extends AbstractProducer {

    public ProducaoProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void publishPedidoConfirmado(PedidoDto pedido, String correlationId, String usuario) {
        publish("pedidos.pedido.confirmado", pedido, correlationId, usuario);
    }

    public void publishPedidoPronto(PedidoDto pedido, String correlationId, String usuario) {
        publish("atendimento.pedido.pronto", pedido, correlationId, usuario);
    }
}
