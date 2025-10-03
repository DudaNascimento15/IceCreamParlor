package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.messaging_rabbitmq.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class ClienteProducer {

    public void publishPedidoPronto(ClienteEvents.PedidoPronto evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("cliente.pedido.pronto", evento, correlationId, usuario);
    }

    public void publishPedidoDespachado(ClienteEvents.PedidoDespachado evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("cliente.pedido.despachado", evento, correlationId, usuario);
    }

    public void publishPedidoACaminho(ClienteEvents.PedidoACaminho evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("cliente.pedido.a_caminho", evento, correlationId, usuario);
    }

    public void publishPedidoEntregue(ClienteEvents.PedidoEntregue evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("cliente.pedido.entregue", evento, correlationId, usuario);
    }
}