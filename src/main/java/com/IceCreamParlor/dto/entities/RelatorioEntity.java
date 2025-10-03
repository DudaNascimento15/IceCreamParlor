package com.IceCreamParlor.dto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "relatorios")
@Getter
@Setter
public class RelatorioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_evento", nullable = false)
    private String nomeEvento;

    @Column(name = "conteudo", columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(name = "recebido_em", nullable = false)
    private OffsetDateTime recebidoEm = OffsetDateTime.now();

    public RelatorioEntity(String nomeEvento, String conteudoJson) {
    }

    protected void Relatorio() {}

    public void Relatorio(String nomeEvento, String conteudo) {
        this.nomeEvento = nomeEvento;
        this.conteudo = conteudo;
    }

}
