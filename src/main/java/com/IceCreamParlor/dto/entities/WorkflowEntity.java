package com.IceCreamParlor.dto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow")
@Getter
@Setter
public class WorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "cliente_id", nullable = false, length = 100)
    private String clienteId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 20)
    private String status = "INICIADO";

    @Column(name = "pagamento_ok")
    private Boolean pagamentoOk = false;

    @Column(name = "estoque_ok")
    private Boolean estoqueOk = false;

    @Column(name = "confirmado_em")
    private OffsetDateTime confirmadoEm;

    public WorkflowEntity() {
    }

    public WorkflowEntity(UUID pedidoId, String clienteId, BigDecimal valorTotal) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.total = valorTotal;
        this.status = "INICIADO";
        this.pagamentoOk = false;
        this.estoqueOk = false;
    }
}