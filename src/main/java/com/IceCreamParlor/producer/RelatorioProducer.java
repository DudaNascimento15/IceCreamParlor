package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.entities.RelatorioEntity;
import com.IceCreamParlor.repositories.RelatorioRepository;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioProducer {

    private final RelatorioRepository repository;
    private final EventPublisher eventPublisher;

    public void publishRelatorio(String conteudo, String correlationId, String usuario) {
        RelatorioEntity relatorio = new RelatorioEntity("relatorio.novo", conteudo);
        repository.save(relatorio);
        eventPublisher.publish("relatorio.novo", relatorio, Map.of("x-user", usuario, "x-event-type", "relatorio.novo"));
    }
}