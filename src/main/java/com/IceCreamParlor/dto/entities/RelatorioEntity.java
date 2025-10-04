package com.IceCreamParlor.dto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "relatorio")
@Getter
@Setter
public class RelatorioEntity {

    @Id
    @UuidGenerator               // Hibernate 6 – gera UUID no Java
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "nome_evento", nullable = false)
    private String nomeEvento;

    @Column(name = "conteudo", columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "recebido_em", nullable = false)
    private OffsetDateTime recebidoEm = OffsetDateTime.now();

    public RelatorioEntity() {}

    public RelatorioEntity(String nomeEvento, String conteudo) {
        this.nomeEvento = nomeEvento;
        this.conteudo = conteudo;
        this.recebidoEm = OffsetDateTime.now();
    }

}
