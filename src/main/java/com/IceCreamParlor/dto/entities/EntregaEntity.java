package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "entrega")
@Getter
@Setter
public class EntregaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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