package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow")
@Getter
@Setter
public class WorkflowEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "cliente_id", nullable = false, length = 100)
    private String clienteId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "pagamento_ok")
    private Boolean pagamentoOk;

    @Column(name = "estoque_ok")
    private Boolean estoqueOk;

    @Column(name = "confirmado_em")
    private OffsetDateTime confirmadoEm;


    protected WorkflowEntity() {
    }

    ;

    public WorkflowEntity(UUID pedidoId, String clienteId, BigDecimal valorTotal) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.total = valorTotal;
    }

}
