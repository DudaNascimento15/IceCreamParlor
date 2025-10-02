package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "entrega")
@Getter
@Setter
public class EntregaEntity {

    @Id
    @Column(name = "pedido_id")
    private UUID pedidoId;

    @Column(nullable = false)
    private String status; // Ex: CRIADO, DESPACHADO, A_CAMINHO, ENTREGUE

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm = OffsetDateTime.now();

    public EntregaEntity() {
    }

    public EntregaEntity(String pedidoId, String criado) {
        this.pedidoId = UUID.fromString(pedidoId);
        this.status = criado;
    }
}
