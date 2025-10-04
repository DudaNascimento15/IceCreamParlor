package com.IceCreamParlor.producer;

import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.messaging.MessagingRabbitmqApplication;
import org.springframework.stereotype.Service;

@Service
public class CaixaProducer {
    public void publishPagamentoAprovado(CaixaEvents.PagamentoAprovado evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("caixa.pagamento.aprovado", evento, correlationId, usuario);
    }

    public void publishPagamentoNegado(CaixaEvents.PagamentoNegado evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("caixa.pagamento.negado", evento, correlationId, usuario);
    }

     public void publishPagamentoIniciado(CaixaEvents.PagamentoIniciado evento, String correlationId, String usuario) {
        MessagingRabbitmqApplication.publish("caixa.pagamento.iniciado", evento, correlationId, usuario);
    }
}