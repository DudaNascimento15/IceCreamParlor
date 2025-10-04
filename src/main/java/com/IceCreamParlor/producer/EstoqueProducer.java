package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.EstoqueEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EstoqueProducer {

    private final EventPublisher eventPublisher;

    public void enviarReservaConfirmada(EstoqueEvents.ReservaConfirmada evento, String correlationId, String usuario) {
        eventPublisher.publish("estoque.reserva.confirmada", evento, Map.of("x-user", usuario, "x-event-type", "estoque.reserva.confirmada"));
    }

    public void enviarReservaNegada(EstoqueEvents.ReservaNegada evento, String correlationId, String usuario) {
        eventPublisher.publish("estoque.reserva.negada", evento, Map.of("x-user", usuario, "x-event-type", "estoque.reserva.negada"));
    }
}