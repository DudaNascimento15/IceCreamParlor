package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.messaging.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class ProducaoProducer {
    public void publishPedidoConfirmado(ProducaoEvents.PedidoConfirmado evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("pedidos.pedido.confirmado", evento, correlationId, usuario);
    }

    public void publishPedidoPronto(ProducaoEvents.PedidoPronto evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("pedidos.pedido.pronto", evento, correlationId, usuario); // Ajustado para pedidos.#
    }
}