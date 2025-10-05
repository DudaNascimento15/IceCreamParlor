package com.IceCreamParlor.dto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "uuid")
    private UUID pedidoId;

    @Column(name = "cliente_id", nullable = false, length = 50)
    private String clienteId;

    @Column(name = "mensagem", nullable = false, length = 255)
    private String mensagem;

    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm = OffsetDateTime.now();

    public ClienteEntity() {
    }

    public ClienteEntity(UUID pedidoId, String clienteId, String mensagem) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.mensagem = mensagem;
        this.criadoEm = OffsetDateTime.now();
    }
}