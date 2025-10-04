package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClienteProducer {

    private final EventPublisher eventPublisher;

    public void publishPedidoPronto(ClienteEvents.PedidoPronto evento, String correlationId, String usuario) {
        eventPublisher.publish("cliente.pedido.pronto", evento, Map.of("x-user", usuario, "x-event-type", "cliente.pedido.pronto"));
    }

    public void publishPedidoDespachado(ClienteEvents.PedidoDespachado evento, String correlationId, String usuario) {
        eventPublisher.publish("cliente.pedido.despachado", evento, Map.of("x-user", usuario, "x-event-type", "cliente.pedido.despachado"));
    }

    public void publishPedidoACaminho(ClienteEvents.PedidoACaminho evento, String correlationId, String usuario) {
        eventPublisher.publish("cliente.pedido.a_caminho", evento, Map.of("x-user", usuario, "x-event-type", "cliente.pedido.a_caminho"));
    }

    public void publishPedidoEntregue(ClienteEvents.PedidoEntregue evento, String correlationId, String usuario) {
        eventPublisher.publish("cliente.pedido.entregue", evento, Map.of("x-user", usuario, "x-event-type", "cliente.pedido.entregue"));
    }
}