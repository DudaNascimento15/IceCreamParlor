package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.entities.RelatorioEntity;
import com.IceCreamParlor.dto.repositories.RelatorioRepository;
import com.IceCreamParlor.messaging_rabbitmq.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class RelatorioProducer {
    private final RelatorioRepository repository;

    public RelatorioProducer(RelatorioRepository repository) {
        this.repository = repository;
    }

    public void publishRelatorio(String conteudo, String correlationId, String usuario) {
        RelatorioEntity relatorio = new RelatorioEntity("relatorio.novo", conteudo); // Usando o construtor correto
        repository.save(relatorio);
        MessagingRabbitmqApplication.publish("relatorio.novo", relatorio, correlationId, usuario);
    }
}