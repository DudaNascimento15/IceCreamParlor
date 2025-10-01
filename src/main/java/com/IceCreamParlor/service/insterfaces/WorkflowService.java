package com.IceCreamParlor.service.insterfaces;

import com.IceCreamParlor.dto.entities.SagaState;

import java.math.BigDecimal;
import java.util.UUID;

public interface WorkflowService {

    UUID iniciarPedido(String clienteId, BigDecimal total, String usuario);

    void pagamentoNegado(UUID pedidoId);

    void pagamentoAprovado(UUID pedidoId, String usuario);

    void reservaConfirmada(UUID pedidoId, String usuario);

    void reservaNegada(UUID pedidoId);

    void tentaConfirmar(SagaState sagaState, String usuario);

    void publish(String routingKey, Object payload, String correlationId, String usuario);

}
