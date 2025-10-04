package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;



@Entity
@Table(name = "caixa")
@Getter
@Setter
public class CaixaEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "aprovado_por", length = 50)
    private String aprovadoPor;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public CaixaEntity() {
    }

    public CaixaEntity(UUID pedidoId, String status, BigDecimal valor) {
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.status = status;
    }
}
