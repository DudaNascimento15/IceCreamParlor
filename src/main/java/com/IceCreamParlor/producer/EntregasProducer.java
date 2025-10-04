package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EntregasProducer {

    private final EventPublisher eventPublisher;

    public void publishPedidoDespachado(EntregaEvents.PedidoDespachado evento, String correlationId, String usuario) {
        eventPublisher.publish("entregas.pedido.despachado", evento, Map.of("x-user", usuario, "x-event-type", "entregas.pedido.despachado"));
    }

    public void publishPedidoACaminho(EntregaEvents.PedidoACaminho evento, String correlationId, String usuario) {
        eventPublisher.publish("entregas.pedido.a_caminho", evento, Map.of("x-user", usuario, "x-event-type", "entregas.pedido.a_caminho"));
    }

    public void publishPedidoEntregue(EntregaEvents.PedidoEntregue evento, String correlationId, String usuario) {
        eventPublisher.publish("entregas.pedido.entregue", evento, Map.of("x-user", usuario, "x-event-type", "entregas.pedido.entregue"));
    }
}