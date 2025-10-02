package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EntregasProducer extends AbstractProducer {

    public EntregasProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void publishPedidoCriado(PedidoDto pedido, String correlationId, String usuario) {
        publish("entregas.pedido.criar", pedido, correlationId, usuario);
    }

    public void publishPedidoDespachado(PedidoDto pedido, String correlationId, String usuario) {
        publish("entregas.pedido.despachado", pedido, correlationId, usuario);
    }

    public void publishPedidoACaminho(PedidoDto pedido, String correlationId, String usuario) {
        publish("entregas.pedido.a_caminho", pedido, correlationId, usuario);
    }

    public void publishPedidoEntregue(PedidoDto pedido, String correlationId, String usuario) {
        publish("entregas.pedido.entregue", pedido, correlationId, usuario);
    }
}
