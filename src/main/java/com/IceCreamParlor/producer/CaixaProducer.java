package com.IceCreamParlor.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CaixaProducer extends AbstractProducer {

    public CaixaProducer(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void publishPagamentoAprovado(PedidoDto pedido, String correlationId, String usuario) {
        publish("caixa.pagamento.aprovado", pedido, correlationId, usuario);
    }

    public void publishPagamentoNegado(PedidoDto pedido, String correlationId, String usuario) {
        publish("caixa.pagamento.negado", pedido, correlationId, usuario);
    }
}
