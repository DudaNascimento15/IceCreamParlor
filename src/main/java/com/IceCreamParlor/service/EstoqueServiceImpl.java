package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.EstoqueEntity;
import com.IceCreamParlor.dto.events.EstoqueEvents;
import com.IceCreamParlor.dto.repositories.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EstoqueServiceImpl {

    private final EstoqueRepository repository;
    //private final Estoqu producer;
    private final Random random = new Random();

    public EstoqueServiceImpl(EstoqueRepository repository, EstoqueProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public void processarReserva(EstoqueEvents.ReservaSolicitada evento) {
        boolean sucesso = random.nextDouble() < 0.8;

        if (sucesso) {
            var reserva = new EstoqueEntity(evento.pedidoId(), "CONFIRMADA", null);
            repository.save(reserva);
            producer.enviarReservaConfirmada(new EstoqueEvents.ReservaConfirmada(evento.pedidoId()));
            System.out.println("✅ Estoque reservado com sucesso: " + evento.pedidoId());
        } else {
            var motivo = "Sem estoque suficiente";
            var reserva = new EstoqueEntity(evento.pedidoId(), "NEGADA", motivo);
            repository.save(reserva);
            producer.enviarReservaNegada(new EstoqueEvents.ReservaNegada(evento.pedidoId(), motivo));
            System.out.println("Falha na reserva de estoque: " + evento.pedidoId());
        }
    }
}
