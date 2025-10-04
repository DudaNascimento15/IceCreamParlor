package com.IceCreamParlor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.listener.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    // Exchanges e constantes
    public static final String EXCHANGE = "sorv.ex";
    public static final String DLX = "x.dlx"; // Dead Letter Exchange dedicada

    public static final String Q_CAIXA = "q.caixa";
    public static final String Q_ESTOQUE = "q.estoque";
    public static final String Q_PRODUCAO = "q.producao";
    public static final String Q_ENTREGAS = "q.entregas";
    public static final String Q_CLIENTE = "q.cliente";
    public static final String Q_RELATORIO = "q.relatorio";
    public static final String Q_WORKFLOW = "q.workflow";

    // Dead Letter Queues genéricas
    public static final String DLQ_CAIXA = "q.caixa.dlq";
    public static final String DLQ_ESTOQUE = "q.estoque.dlq";
    public static final String DLQ_PRODUCAO = "q.producao.dlq";
    public static final String DLQ_ENTREGAS = "q.entregas.dlq";

    // Filas específicas do fluxograma
    public static final String Q_CAIXA_PAGAMENTO_INICIADO = "q.caixa.pagamento.iniciado";
    public static final String Q_CAIXA_PAGAMENTO_APROVADO = "q.caixa.pagamento.aprovado";
    public static final String Q_CAIXA_PAGAMENTO_NEGADO = "q.caixa.pagamento.negado";
    public static final String Q_CLIENTE_PEDIDO_PRONTO = "q.cliente.pedido.pronto";
    public static final String Q_CLIENTE_PEDIDO_DESPACHADO = "q.cliente.pedido.despachado";
    public static final String Q_CLIENTE_PEDIDO_A_CAMINHO = "q.cliente.pedido.a_caminho";
    public static final String Q_CLIENTE_PEDIDO_ENTREGUE = "q.cliente.pedido.entregue";
    public static final String Q_ENTREGAS_PEDIDO_CRIAR = "q.entregas.pedido.criar";
    public static final String Q_ENTREGAS_PEDIDO_DESPACHADO = "q.entregas.pedido.despachado";
    public static final String Q_ENTREGAS_PEDIDO_A_CAMINHO = "q.entregas.pedido.a_caminho";
    public static final String Q_ENTREGAS_PEDIDO_ENTREGUE = "q.entregas.pedido.entregue";
    public static final String Q_PRODUCAO_PEDIDO_CONFIRMADO = "q.producao.pedido.confirmado";
    public static final String Q_PRODUCAO_PEDIDO_PRONTO = "q.producao.pedido.pronto";
    public static final String Q_RELATORIO_EVENTO_RECEBIDO = "q.relatorio.evento.recebido";

    // DLQs específicas
    public static final String DLQ_CAIXA_PAGAMENTO_INICIADO = "q.caixa.pagamento.iniciado.dlq";
    public static final String DLQ_CAIXA_PAGAMENTO_APROVADO = "q.caixa.pagamento.aprovado.dlq";
    public static final String DLQ_CAIXA_PAGAMENTO_NEGADO = "q.caixa.pagamento.negado.dlq";
    public static final String DLQ_CLIENTE_PEDIDO_PRONTO = "q.cliente.pedido.pronto.dlq";
    public static final String DLQ_CLIENTE_PEDIDO_DESPACHADO = "q.cliente.pedido.despachado.dlq";
    public static final String DLQ_CLIENTE_PEDIDO_A_CAMINHO = "q.cliente.pedido.a_caminho.dlq";
    public static final String DLQ_CLIENTE_PEDIDO_ENTREGUE = "q.cliente.pedido.entregue.dlq";
    public static final String DLQ_ENTREGAS_PEDIDO_CRIAR = "q.entregas.pedido.criar.dlq";
    public static final String DLQ_ENTREGAS_PEDIDO_DESPACHADO = "q.entregas.pedido.despachado.dlq";
    public static final String DLQ_ENTREGAS_PEDIDO_A_CAMINHO = "q.entregas.pedido.a_caminho.dlq";
    public static final String DLQ_ENTREGAS_PEDIDO_ENTREGUE = "q.entregas.pedido.entregue.dlq";
    public static final String DLQ_PRODUCAO_PEDIDO_CONFIRMADO = "q.producao.pedido.confirmado.dlq";
    public static final String DLQ_PRODUCAO_PEDIDO_PRONTO = "q.producao.pedido.pronto.dlq";
    public static final String DLQ_RELATORIO_EVENTO_RECEBIDO = "q.relatorio.evento.recebido.dlq";

    // TTL padrão: 30 minutos
    private static final long TTL_MILLIS = 1800000L;

    // Exchange principal
    @Bean
    public TopicExchange exchange() {
        return ExchangeBuilder
            .topicExchange(EXCHANGE)
            .durable(true)
            .build();
    }

    // Dead Letter Exchange
    @Bean
    public TopicExchange dlExchange() {
        return ExchangeBuilder
            .topicExchange(DLX)
            .durable(true)
            .build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true);
        template.setChannelTransacted(false);
        return template;
    }

    // ========== FILAS E DLQs GENÉRICAS ==========
    @Bean
    public Queue queueCaixa() {
        return QueueBuilder.durable(Q_CAIXA)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_CAIXA)
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqCaixa() {
        return QueueBuilder.durable(DLQ_CAIXA).build();
    }

    @Bean
    public Binding bindingCaixa() {
        return BindingBuilder
            .bind(queueCaixa())
            .to(exchange())
            .with("caixa.#");
    }

    @Bean
    public Queue queueEstoque() {
        return QueueBuilder.durable(Q_ESTOQUE)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_ESTOQUE)
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqEstoque() {
        return QueueBuilder.durable(DLQ_ESTOQUE).build();
    }

    @Bean
    public Binding bindingEstoque() {
        return BindingBuilder
            .bind(queueEstoque())
            .to(exchange())
            .with("estoque.#");
    }

    @Bean
    public Queue queueProducao() {
        return QueueBuilder.durable(Q_PRODUCAO)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_PRODUCAO)
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqProducao() {
        return QueueBuilder.durable(DLQ_PRODUCAO).build();
    }

    @Bean
    public Binding bindingProducao() {
        return BindingBuilder
            .bind(queueProducao())
            .to(exchange())
            .with("producao.#");
    }

    @Bean
    public Queue queueEntregas() {
        return QueueBuilder.durable(Q_ENTREGAS)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_ENTREGAS)
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Queue dlqEntregas() {
        return QueueBuilder.durable(DLQ_ENTREGAS).build();
    }

    @Bean
    public Binding bindingEntregas() {
        return BindingBuilder
            .bind(queueEntregas())
            .to(exchange())
            .with("entregas.#");
    }

    @Bean
    public Queue queueCliente() {
        return QueueBuilder.durable(Q_CLIENTE)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_ENTREGAS) // Usando DLQ_ENTREGAS como exemplo, ajuste se necessário
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingCliente() {
        return BindingBuilder
            .bind(queueCliente())
            .to(exchange())
            .with("cliente.#");
    }

    @Bean
    public Queue queueRelatorio() {
        return QueueBuilder.durable(Q_RELATORIO)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_ENTREGAS) // Ajuste para DLQ específica se necessário
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingRelatorio() {
        return BindingBuilder
            .bind(queueRelatorio())
            .to(exchange())
            .with("#"); // Escuta todos os eventos
    }

    @Bean
    public Queue queueWorkflow() {
        return QueueBuilder.durable(Q_WORKFLOW)
            .withArgument("x-dead-letter-exchange", DLX)
            .withArgument("x-dead-letter-routing-key", DLQ_ENTREGAS) // Ajuste se necessário
            .withArgument("x-message-ttl", TTL_MILLIS)
            .withArgument("x-queue-type", "classic")
            .build();
    }

    @Bean
    public Binding bindingWorkflowPagamento() {
        return BindingBuilder
            .bind(queueWorkflow())
            .to(exchange())
            .with("caixa.pagamento.aprovado");
    }

    @Bean
    public Binding bindingWorkflowEstoque() {
        return BindingBuilder
            .bind(queueWorkflow())
            .to(exchange())
            .with("estoque.reserva.confirmada");
    }

    // ========== FILAS ESPECÍFICAS DO FLUXOGRAMA ==========
    @Bean
    public Queue qCaixaPagamentoIniciado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CAIXA_PAGAMENTO_INICIADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CAIXA_PAGAMENTO_INICIADO).withArguments(args).build();
    }

    @Bean
    public Queue qCaixaPagamentoIniciadoDlq() {
        return QueueBuilder.durable(DLQ_CAIXA_PAGAMENTO_INICIADO).build();
    }

    @Bean
    public Binding bindingCaixaPagamentoIniciado() {
        return BindingBuilder
            .bind(qCaixaPagamentoIniciado())
            .to(exchange())
            .with("caixa.pagamento.iniciado");
    }

    @Bean
    public Binding bindingCaixaPagamentoIniciadoDlq() {
        return BindingBuilder
            .bind(qCaixaPagamentoIniciadoDlq())
            .to(dlExchange())
            .with("dl.caixa.pagamento.iniciado");
    }

    @Bean
    public Queue qCaixaPagamentoAprovado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CAIXA_PAGAMENTO_APROVADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CAIXA_PAGAMENTO_APROVADO).withArguments(args).build();
    }

    @Bean
    public Queue qCaixaPagamentoAprovadoDlq() {
        return QueueBuilder.durable(DLQ_CAIXA_PAGAMENTO_APROVADO).build();
    }

    @Bean
    public Binding bindingCaixaPagamentoAprovado() {
        return BindingBuilder
            .bind(qCaixaPagamentoAprovado())
            .to(exchange())
            .with("caixa.pagamento.aprovado");
    }

    @Bean
    public Binding bindingCaixaPagamentoAprovadoDlq() {
        return BindingBuilder
            .bind(qCaixaPagamentoAprovadoDlq())
            .to(dlExchange())
            .with("dl.caixa.pagamento.aprovado");
    }

    @Bean
    public Queue qCaixaPagamentoNegado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CAIXA_PAGAMENTO_NEGADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CAIXA_PAGAMENTO_NEGADO).withArguments(args).build();
    }

    @Bean
    public Queue qCaixaPagamentoNegadoDlq() {
        return QueueBuilder.durable(DLQ_CAIXA_PAGAMENTO_NEGADO).build();
    }

    @Bean
    public Binding bindingCaixaPagamentoNegado() {
        return BindingBuilder
            .bind(qCaixaPagamentoNegado())
            .to(exchange())
            .with("caixa.pagamento.negado");
    }

    @Bean
    public Binding bindingCaixaPagamentoNegadoDlq() {
        return BindingBuilder
            .bind(qCaixaPagamentoNegadoDlq())
            .to(dlExchange())
            .with("dl.caixa.pagamento.negado");
    }

    @Bean
    public Queue qClientePedidoPronto() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CLIENTE_PEDIDO_PRONTO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CLIENTE_PEDIDO_PRONTO).withArguments(args).build();
    }

    @Bean
    public Queue qClientePedidoProntoDlq() {
        return QueueBuilder.durable(DLQ_CLIENTE_PEDIDO_PRONTO).build();
    }

    @Bean
    public Binding bindingClientePedidoPronto() {
        return BindingBuilder
            .bind(qClientePedidoPronto())
            .to(exchange())
            .with("cliente.pedido.pronto");
    }

    @Bean
    public Binding bindingClientePedidoProntoDlq() {
        return BindingBuilder
            .bind(qClientePedidoProntoDlq())
            .to(dlExchange())
            .with("dl.cliente.pedido.pronto");
    }

    @Bean
    public Queue qClientePedidoDespachado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CLIENTE_PEDIDO_DESPACHADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CLIENTE_PEDIDO_DESPACHADO).withArguments(args).build();
    }

    @Bean
    public Queue qClientePedidoDespachadoDlq() {
        return QueueBuilder.durable(DLQ_CLIENTE_PEDIDO_DESPACHADO).build();
    }

    @Bean
    public Binding bindingClientePedidoDespachado() {
        return BindingBuilder
            .bind(qClientePedidoDespachado())
            .to(exchange())
            .with("cliente.pedido.despachado");
    }

    @Bean
    public Binding bindingClientePedidoDespachadoDlq() {
        return BindingBuilder
            .bind(qClientePedidoDespachadoDlq())
            .to(dlExchange())
            .with("dl.cliente.pedido.despachado");
    }

    @Bean
    public Queue qClientePedidoACaminho() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CLIENTE_PEDIDO_A_CAMINHO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CLIENTE_PEDIDO_A_CAMINHO).withArguments(args).build();
    }

    @Bean
    public Queue qClientePedidoACaminhoDlq() {
        return QueueBuilder.durable(DLQ_CLIENTE_PEDIDO_A_CAMINHO).build();
    }

    @Bean
    public Binding bindingClientePedidoACaminho() {
        return BindingBuilder
            .bind(qClientePedidoACaminho())
            .to(exchange())
            .with("cliente.pedido.a_caminho");
    }

    @Bean
    public Binding bindingClientePedidoACaminhoDlq() {
        return BindingBuilder
            .bind(qClientePedidoACaminhoDlq())
            .to(dlExchange())
            .with("dl.cliente.pedido.a_caminho");
    }

    @Bean
    public Queue qClientePedidoEntregue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_CLIENTE_PEDIDO_ENTREGUE);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_CLIENTE_PEDIDO_ENTREGUE).withArguments(args).build();
    }

    @Bean
    public Queue qClientePedidoEntregueDlq() {
        return QueueBuilder.durable(DLQ_CLIENTE_PEDIDO_ENTREGUE).build();
    }

    @Bean
    public Binding bindingClientePedidoEntregue() {
        return BindingBuilder
            .bind(qClientePedidoEntregue())
            .to(exchange())
            .with("cliente.pedido.entregue");
    }

    @Bean
    public Binding bindingClientePedidoEntregueDlq() {
        return BindingBuilder
            .bind(qClientePedidoEntregueDlq())
            .to(dlExchange())
            .with("dl.cliente.pedido.entregue");
    }

    @Bean
    public Queue qEntregasPedidoCriar() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_ENTREGAS_PEDIDO_CRIAR);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_ENTREGAS_PEDIDO_CRIAR).withArguments(args).build();
    }

    @Bean
    public Queue qEntregasPedidoCriarDlq() {
        return QueueBuilder.durable(DLQ_ENTREGAS_PEDIDO_CRIAR).build();
    }

    @Bean
    public Binding bindingEntregasPedidoCriar() {
        return BindingBuilder
            .bind(qEntregasPedidoCriar())
            .to(exchange())
            .with("entregas.pedido.criar");
    }

    @Bean
    public Binding bindingEntregasPedidoCriarDlq() {
        return BindingBuilder
            .bind(qEntregasPedidoCriarDlq())
            .to(dlExchange())
            .with("dl.entregas.pedido.criar");
    }

    @Bean
    public Queue qEntregasPedidoDespachado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_ENTREGAS_PEDIDO_DESPACHADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_ENTREGAS_PEDIDO_DESPACHADO).withArguments(args).build();
    }

    @Bean
    public Queue qEntregasPedidoDespachadoDlq() {
        return QueueBuilder.durable(DLQ_ENTREGAS_PEDIDO_DESPACHADO).build();
    }

    @Bean
    public Binding bindingEntregasPedidoDespachado() {
        return BindingBuilder
            .bind(qEntregasPedidoDespachado())
            .to(exchange())
            .with("entregas.pedido.despachado");
    }

    @Bean
    public Binding bindingEntregasPedidoDespachadoDlq() {
        return BindingBuilder
            .bind(qEntregasPedidoDespachadoDlq())
            .to(dlExchange())
            .with("dl.entregas.pedido.despachado");
    }

    @Bean
    public Queue qEntregasPedidoACaminho() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_ENTREGAS_PEDIDO_A_CAMINHO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_ENTREGAS_PEDIDO_A_CAMINHO).withArguments(args).build();
    }

    @Bean
    public Queue qEntregasPedidoACaminhoDlq() {
        return QueueBuilder.durable(DLQ_ENTREGAS_PEDIDO_A_CAMINHO).build();
    }

    @Bean
    public Binding bindingEntregasPedidoACaminho() {
        return BindingBuilder
            .bind(qEntregasPedidoACaminho())
            .to(exchange())
            .with("entregas.pedido.a_caminho");
    }

    @Bean
    public Binding bindingEntregasPedidoACaminhoDlq() {
        return BindingBuilder
            .bind(qEntregasPedidoACaminhoDlq())
            .to(dlExchange())
            .with("dl.entregas.pedido.a_caminho");
    }

    @Bean
    public Queue qEntregasPedidoEntregue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_ENTREGAS_PEDIDO_ENTREGUE);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_ENTREGAS_PEDIDO_ENTREGUE).withArguments(args).build();
    }

    @Bean
    public Queue qEntregasPedidoEntregueDlq() {
        return QueueBuilder.durable(DLQ_ENTREGAS_PEDIDO_ENTREGUE).build();
    }

    @Bean
    public Binding bindingEntregasPedidoEntregue() {
        return BindingBuilder
            .bind(qEntregasPedidoEntregue())
            .to(exchange())
            .with("entregas.pedido.entregue");
    }

    @Bean
    public Binding bindingEntregasPedidoEntregueDlq() {
        return BindingBuilder
            .bind(qEntregasPedidoEntregueDlq())
            .to(dlExchange())
            .with("dl.entregas.pedido.entregue");
    }

    @Bean
    public Queue qProducaoPedidoConfirmado() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_PRODUCAO_PEDIDO_CONFIRMADO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_PRODUCAO_PEDIDO_CONFIRMADO).withArguments(args).build();
    }

    @Bean
    public Queue qProducaoPedidoConfirmadoDlq() {
        return QueueBuilder.durable(DLQ_PRODUCAO_PEDIDO_CONFIRMADO).build();
    }

    @Bean
    public Binding bindingProducaoPedidoConfirmado() {
        return BindingBuilder
            .bind(qProducaoPedidoConfirmado())
            .to(exchange())
            .with("producao.pedido.confirmado");
    }

    @Bean
    public Binding bindingProducaoPedidoConfirmadoDlq() {
        return BindingBuilder
            .bind(qProducaoPedidoConfirmadoDlq())
            .to(dlExchange())
            .with("dl.producao.pedido.confirmado");
    }

    @Bean
    public Queue qProducaoPedidoPronto() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_PRODUCAO_PEDIDO_PRONTO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_PRODUCAO_PEDIDO_PRONTO).withArguments(args).build();
    }

    @Bean
    public Queue qProducaoPedidoProntoDlq() {
        return QueueBuilder.durable(DLQ_PRODUCAO_PEDIDO_PRONTO).build();
    }

    @Bean
    public Binding bindingProducaoPedidoPronto() {
        return BindingBuilder
            .bind(qProducaoPedidoPronto())
            .to(exchange())
            .with("producao.pedido.pronto");
    }

    @Bean
    public Binding bindingProducaoPedidoProntoDlq() {
        return BindingBuilder
            .bind(qProducaoPedidoProntoDlq())
            .to(dlExchange())
            .with("dl.producao.pedido.pronto");
    }

    @Bean
    public Queue qRelatorioEventoRecebido() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_RELATORIO_EVENTO_RECEBIDO);
        args.put("x-message-ttl", TTL_MILLIS);
        args.put("x-queue-type", "classic");
        return QueueBuilder.durable(Q_RELATORIO_EVENTO_RECEBIDO).withArguments(args).build();
    }

    @Bean
    public Queue qRelatorioEventoRecebidoDlq() {
        return QueueBuilder.durable(DLQ_RELATORIO_EVENTO_RECEBIDO).build();
    }

    @Bean
    public Binding bindingRelatorioEventoRecebido() {
        return BindingBuilder
            .bind(qRelatorioEventoRecebido())
            .to(exchange())
            .with("relatorio.evento.recebido");
    }

    @Bean
    public Binding bindingRelatorioEventoRecebidoDlq() {
        return BindingBuilder
            .bind(qRelatorioEventoRecebidoDlq())
            .to(dlExchange())
            .with("dl.relatorio.evento.recebido");
    }

    // ========== CONTAINER FACTORY COM RETRIES ==========
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setDefaultRequeueRejected(false); // Vai para DLQ após retries

        // Retry: 3 tentativas com backoff exponencial (1s, 2s, 4s)
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000L);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(10000L);
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        factory.setRetryTemplate(retryTemplate);
        return factory;
    }
}