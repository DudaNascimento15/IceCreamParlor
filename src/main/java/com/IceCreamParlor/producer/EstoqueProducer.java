package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.EstoqueEvents;
import com.IceCreamParlor.messaging_rabbitmq.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class EstoqueProducer {

    public void enviarReservaConfirmada(EstoqueEvents.ReservaConfirmada evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("estoque.reserva.confirmada", evento, correlationId, usuario);
    }

    public void enviarReservaNegada(EstoqueEvents.ReservaNegada evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("estoque.reserva.negada", evento, correlationId, usuario);
    }
}