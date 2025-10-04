package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusProducaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "producao")
@Getter
@Setter
@RequiredArgsConstructor
public class ProducaoEntity {

    @Id
    @Column(name = "pedido_id")
    private UUID pedidoId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProducaoEnum status;

    @Column(name = "iniciado_em", nullable = false)
    private OffsetDateTime iniciadoEm = OffsetDateTime.now();

    @Column(name = "finalizado_em")
    private OffsetDateTime finalizadoEm;

    public ProducaoEntity(UUID uuid, String emPreparo) {
    }

    public ProducaoEntity() {}

    public ProducaoEntity(UUID pedidoId, StatusProducaoEnum status) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.iniciadoEm = OffsetDateTime.now();
    }

    public void finalizarProducao() {
        this.status = StatusProducaoEnum.PRONTO;
        this.finalizadoEm = OffsetDateTime.now();
    }


}
