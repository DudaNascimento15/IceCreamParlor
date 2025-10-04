package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;


import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "entrega")
@Getter
@Setter
public class EntregaEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public EntregaEntity() {
    }

    public EntregaEntity(String pedidoId, StatusEntregaEnum status) {
        this.pedidoId = UUID.fromString(pedidoId);
        this.status = status.toString();
        this.criadoEm = OffsetDateTime.now();
    }

    public EntregaEntity(UUID pedidoId, StatusEntregaEnum status) {
        this.pedidoId = pedidoId;
        this.status = status.toString();
        this.criadoEm = OffsetDateTime.now();
    }

    public void atualizarStatus(StatusEntregaEnum novoStatus) {
        this.status = novoStatus.toString();
        this.criadoEm = OffsetDateTime.now();
    }
}
