package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CaixaProducer {

    private final EventPublisher eventPublisher;

    public void publishPagamentoAprovado(CaixaEvents.PagamentoAprovado evento, String correlationId, String usuario) {
        eventPublisher.publish("caixa.pagamento.aprovado", evento, Map.of("x-user", usuario, "x-event-type", "caixa.pagamento.aprovado"));
    }

    public void publishPagamentoNegado(CaixaEvents.PagamentoNegado evento, String correlationId, String usuario) {
        eventPublisher.publish("caixa.pagamento.negado", evento, Map.of("x-user", usuario, "x-event-type", "caixa.pagamento.negado"));
    }

    public void publishPagamentoIniciado(CaixaEvents.PagamentoIniciado evento, String correlationId, String usuario) {
        eventPublisher.publish("caixa.pagamento.iniciado", evento, Map.of("x-user", usuario, "x-event-type", "caixa.pagamento.iniciado"));
    }
}