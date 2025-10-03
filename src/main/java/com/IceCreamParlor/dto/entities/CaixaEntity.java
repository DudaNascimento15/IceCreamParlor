package com.IceCreamParlor.dto.entities;

import com.IceCreamParlor.dto.enums.StatusCaixaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "caixa")
@Getter
@Setter
public class CaixaEntity {

   @Id
   @Column(name = "pedido_id", columnDefinition = "uuid", nullable = false)
   private String pedidoId;

    @Column(name = "status", columnDefinition = "varchar(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCaixaEnum status;

    @Column(name = "motivo", columnDefinition = "varchar(255)")
    private String motivoNegacao;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public CaixaEntity(String pedidoId, String status,BigDecimal valor) {
        this.pedidoId = pedidoId;
        this.status = StatusCaixaEnum.valueOf(status);
        this.valor = valor;
    }

    protected CaixaEntity() {
    }


}
