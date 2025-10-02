package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RelatorioProducer extends AbstractProducer {

    private final RelatorioRepository repository;

    public RelatorioProducer(RabbitTemplate rabbitTemplate, RelatorioRepository repository) {
        super(rabbitTemplate);
        this.repository = repository;
    }

    public void publishRelatorio(String conteudo, String correlationId, String usuario) {
        Relatorio relatorio = Relatorio.builder()
                .conteudo(conteudo)
                .build();

        repository.save(relatorio);

        publish("relatorio.novo", relatorio, correlationId, usuario);
    }
}
