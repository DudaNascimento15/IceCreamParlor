package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusEstoqueEnum;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "estoque_reservas")
public class EstoqueEntity {

    @Id
    @Column(name = "pedido_id")
    private UUID pedidoId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEstoqueEnum status; // CONFIRMADA, NEGADA

    @Column(name = "motivo")
    private String motivo; // opcional, só se negado

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public EstoqueEntity(UUID uuid, String confirmada, Object o) {
    }

    protected void EntregaEntity() {}

    public void EntregaEntity(UUID pedidoId, String status, String motivo) {
        this.pedidoId = pedidoId;
        this.status = StatusEstoqueEnum.valueOf(status);
        this.motivo = motivo;
    }

    public UUID getPedidoId() {
        return pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivo() {
        return motivo;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}

