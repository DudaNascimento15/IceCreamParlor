package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class WorkflowEntity {

    @Id
    @Column(name = "pedido_id", columnDefinition = "uuid", nullable = false)
    private UUID pedidoId;

    @Column(name = "cliente_id", columnDefinition = "uuid", nullable = false)
    private String clienteId;

    @Column(name = "valor_total", precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "pagamento_aprovado", columnDefinition = "boolean")
    private boolean pagamentoAprovado;

    @Column(name = "estoque_Reservado", columnDefinition = "boolean")
    private boolean estoqueReservado;

    @Column(name = "criado_em", columnDefinition = "timestamp with time zone", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    protected WorkflowEntity() {
    }

    ;

    public WorkflowEntity(UUID pedidoId, String clienteId, BigDecimal valorTotal) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.valorTotal = valorTotal;

    }

}
