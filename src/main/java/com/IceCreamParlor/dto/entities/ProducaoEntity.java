package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusProducaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "producao")
@Getter
@Setter
public class ProducaoEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "iniciado_em", nullable = false)
    private OffsetDateTime iniciadoEm = OffsetDateTime.now();

    @Column(name = "finalizado_em")
    private OffsetDateTime finalizadoEm;

    public ProducaoEntity() {
    }

    public ProducaoEntity(UUID pedidoId, String status) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.iniciadoEm = OffsetDateTime.now();
    }

    public void finalizarProducao() {
        this.status = StatusProducaoEnum.PRONTO.toString();
        this.finalizadoEm = OffsetDateTime.now();
    }


}
