package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "saga_workflow")
@Getter
@Setter
public class SagaState {

    private UUID pedidoId;

    private String clienteId;

    private BigDecimal valorTotal;

    private boolean pagamentoAprovado;

    private boolean estoqueReservado;

    private OffsetDateTime criadoEm = OffsetDateTime.now();

    protected SagaState() {
    }

    ;

    public SagaState(UUID pedidoId, String clienteId, BigDecimal valorTotal) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;

    }

}
