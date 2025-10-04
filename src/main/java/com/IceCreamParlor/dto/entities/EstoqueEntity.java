package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusEstoqueEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "estoque_reservas")
@Getter
@Setter
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

    public EstoqueEntity() {}

    public EstoqueEntity(UUID pedidoId, StatusEstoqueEnum status, String motivo) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.motivo = motivo;
        this.criadoEm = OffsetDateTime.now();
    }

    public EstoqueEntity(UUID pedidoId, StatusEstoqueEnum status) {
        this(pedidoId, status, null);
    }


}

