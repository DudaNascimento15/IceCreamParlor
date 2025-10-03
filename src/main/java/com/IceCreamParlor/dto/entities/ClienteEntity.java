package com.IceCreamParlor.dto.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private UUID pedidoId;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(nullable = false)
    private String mensagem;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();


    protected ClienteEntity() {}

    public ClienteEntity(UUID pedidoId, String clienteId, String mensagem) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.mensagem = mensagem;
    }

}
