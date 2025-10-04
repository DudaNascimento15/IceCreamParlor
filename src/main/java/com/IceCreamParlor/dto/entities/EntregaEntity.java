package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "entrega")
@Getter
@Setter
public class EntregaEntity {

    @Id
    @UuidGenerator               // Hibernate 6 – gera UUID no Java
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id")
    private UUID pedidoId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntregaEnum status;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm = OffsetDateTime.now();

    public EntregaEntity() {}

    public EntregaEntity(String pedidoId, StatusEntregaEnum status) {
        this.pedidoId = UUID.fromString(pedidoId);
        this.status = status;
        this.atualizadoEm = OffsetDateTime.now();
    }

    public EntregaEntity(UUID pedidoId, StatusEntregaEnum status) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.atualizadoEm = OffsetDateTime.now();
    }

    public void atualizarStatus(StatusEntregaEnum novoStatus) {
        this.status = novoStatus;
        this.atualizadoEm = OffsetDateTime.now();
    }
}
