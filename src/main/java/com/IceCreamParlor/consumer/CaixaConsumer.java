package com.IceCreamParlor.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaixaConsumer {

  /*  private final CaixaService pedidoService;
    private final ProcessedEventRepository processed; // tabela message_id único
    private final MessageBus bus;

    public CaixaConsumer(CaixaService pedidoService, ProcessedEventRepository processed, MessageBus bus) {
        this.pedidoService = pedidoService;
        this.processed = processed;
        this.bus = bus;
    }

    @RabbitListener(queues = "q.caixa")
    public void onPagamento(Envelope<CaixaPagamentoIniciadoEvt> env) {
        if (processed.existsByMessageId(env.messageId())) return;
        try {
            // simula regra de pagamento:
            boolean aprovado = processarPagamento(env.data().pedidoId(), env.data().valor());

            if (aprovado) {
                pedidoService.aprovarPagamento(env.data().pedidoId());
                bus.publish("caixa.pagamento.aprovado",
                    new CaixaPagamentoAprovadoEvt(env.data().pedidoId()),
                    env.correlationId(), env.usuario());
            } else {
                pedidoService.negarPagamento(env.data().pedidoId(), "cartão recusado");
                bus.publish("caixa.pagamento.negado",
                    new CaixaPagamentoNegadoEvt(env.data().pedidoId(), "cartão recusado"),
                    env.correlationId(), env.usuario());
            }

            processed.save(new ProcessedEvent(env.messageId()));
        } catch (ReprocessavelException e) {
            // vai para DLX -> q.caixa.retry
            throw new AmqpRejectAndDontRequeueException("retry", e);
        } catch (Exception e) {
            // envenenada → DLQ
            throw new ImmediateAcknowledgeAmqpException("poison", e);
        }
    }

    private boolean processarPagamento(String pedidoId, BigDecimal valor) {
        // Simule uma chamada externa ao gateway de pagamento
        return Math.random() > 0.2; // 80% de chance de aprovar
    }*/
}
