package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String status; // EM_PREPARO, PRONTO

    @Column(name = "iniciado_em", nullable = false)
    private OffsetDateTime iniciadoEm = OffsetDateTime.now();

    @Column(name = "finalizado_em")
    private OffsetDateTime finalizadoEm;

    public ProducaoEntity(UUID uuid, String emPreparo) {
    }

    protected void Producao() {
    }

    public void Producao(UUID pedidoId, String status) {
        this.pedidoId = pedidoId;
        this.status = status;
    }

    public void finalizarProducao() {
        this.status = "PRONTO";
        this.finalizadoEm = OffsetDateTime.now();
    }

}
