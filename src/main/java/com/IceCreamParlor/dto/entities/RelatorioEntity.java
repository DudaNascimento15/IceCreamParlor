package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "relatorio")
@Getter
@Setter
public class RelatorioEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "origem", length = 50)
    private String origem;

    @Column(name = "evento", length = 100)
    private String evento;

    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public RelatorioEntity() {
    }


    public RelatorioEntity(String evento, String payload) {
        this.evento = evento;
        this.payload = payload;
        this.criadoEm = OffsetDateTime.now();
    }

}
