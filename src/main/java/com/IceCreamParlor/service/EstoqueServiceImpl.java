package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.EstoqueEntity;
import com.IceCreamParlor.dto.enums.StatusEstoqueEnum;
import com.IceCreamParlor.dto.events.EstoqueEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.repositories.EstoqueRepository;
import com.IceCreamParlor.producer.EstoqueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstoqueServiceImpl {

    private final EstoqueRepository repository;
    private final EstoqueProducer producer;
    private final Random random = new Random();

    public void processarReserva(WorkflowEvents.ReservaSolicitada evento, String correlationId, String usuario) {
        log.info("Processando reserva de estoque para o pedido: {}", evento.pedidoId());

        boolean sucesso = random.nextDouble() < 0.8; // simula verificação de estoque

        if (sucesso) {
            var reserva = new EstoqueEntity(
                evento.pedidoId(),
                StatusEstoqueEnum.CONFIRMADO.toString()
            );
            repository.save(reserva);
            EstoqueEvents.ReservaConfirmada confirmada =
                new EstoqueEvents.ReservaConfirmada(evento.pedidoId(), evento.clienteId());

            producer.enviarReservaConfirmada(confirmada, correlationId, usuario);

            log.info("✅ Estoque reservado com sucesso - pedidoId: {}", evento.pedidoId());
        } else {
            var motivo = "Sem estoque suficiente";
            var reserva = new EstoqueEntity(
                evento.pedidoId(),
                StatusEstoqueEnum.NEGADO.toString(),
                motivo
            );
            repository.save(reserva);
            producer.enviarReservaNegada(
                new EstoqueEvents.ReservaNegada(
                    evento.pedidoId(),
                    motivo,
                    evento.clienteId()),
                evento.pedidoId().toString(),
                evento.clienteId());
           log.info("Falha na reserva de estoque: " + evento.pedidoId());
        }
    }
}
