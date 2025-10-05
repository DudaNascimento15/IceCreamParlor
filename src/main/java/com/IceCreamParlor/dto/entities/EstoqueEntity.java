package com.IceCreamParlor.dto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "estoque")
@Getter
@Setter
public class EstoqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "itens_reservados", nullable = false)
    private Boolean itensReservados = true;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "reservado_em", nullable = false)
    private OffsetDateTime reservadoEm = OffsetDateTime.now();

    public EstoqueEntity() {
    }

    public EstoqueEntity(UUID pedidoId, String status, String motivo) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.motivo = motivo;
        this.reservadoEm = OffsetDateTime.now();
    }

    public EstoqueEntity(UUID pedidoId, String status) {
        this(pedidoId, status, null);
    }
}
