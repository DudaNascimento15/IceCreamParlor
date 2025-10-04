package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import com.IceCreamParlor.messaging.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class EntregasProducer {


    private final EventPublisher EventPublisher;

    public EntregasProducer(EventPublisher eventPublisher) {
        EventPublisher = eventPublisher;
    }

    public void publishPedidoDespachado(EntregaEvents.PedidoDespachado evento, String correlationId, String usuario) {
        EventPublisher.publish("entregas.pedido.despachado", evento, correlationId, usuario);
    }

    public void publishPedidoACaminho(EntregaEvents.PedidoACaminho evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("entregas.pedido.a_caminho", evento, correlationId, usuario);
    }

    public void publishPedidoEntregue(EntregaEvents.PedidoEntregue evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("entregas.pedido.entregue", evento, correlationId, usuario);
    }
}