package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProducaoProducer {

    private final EventPublisher eventPublisher;

    public void publishPedidoConfirmado(ProducaoEvents.PedidoConfirmado evento, String correlationId, String usuario) {
        eventPublisher.publish("pedidos.pedido.confirmado", evento, Map.of("x-user", usuario, "x-event-type", "pedidos.pedido.confirmado"));
    }

    public void publishPedidoPronto(ProducaoEvents.PedidoPronto evento, String correlationId, String usuario) {
        eventPublisher.publish("pedidos.pedido.pronto", evento, Map.of("x-user", usuario, "x-event-type", "pedidos.pedido.pronto"));
    }
}