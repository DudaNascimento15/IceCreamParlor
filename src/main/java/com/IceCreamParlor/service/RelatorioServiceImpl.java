package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.RelatorioEntity;
import com.IceCreamParlor.repositories.RelatorioRepository;
import org.springframework.stereotype.Service;

@Service
public class RelatorioServiceImpl {

    private final RelatorioRepository repository;

    public RelatorioServiceImpl(RelatorioRepository repository) {
        this.repository = repository;
    }

    public void salvarEvento(String nomeEvento, String conteudoJson) {
        RelatorioEntity relatorio = new RelatorioEntity(nomeEvento, conteudoJson);
        repository.save(relatorio);
        System.out.println("Evento salvo no relatório: " + nomeEvento);
    }
}
