package com.IceCreamParlor;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_MAIN = "sorv.ex";
    public static final String EXCHANGE_DLX = "sorv.dlx";
    private static final int RETRY_TTL_MS = 15_000;

    @Bean TopicExchange sorvExchange() { return new TopicExchange(EXCHANGE_MAIN, true, false); }
    @Bean DirectExchange sorvDlx() { return new DirectExchange(EXCHANGE_DLX, true, false); }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(conv);
        return t;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory,
        SimpleRabbitListenerContainerFactoryConfigurer configurer,
        Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setPrefetchCount(1);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    // ==================== WORKFLOW ====================
    @Bean
    Queue qWorkflow() {
        return QueueBuilder.durable("q.workflow")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "workflow.dead")
            .build();
    }

    @Bean
    Queue qWorkflowRetry() {
        return QueueBuilder.durable("q.workflow.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "workflow.retry.back")
            .build();
    }

    @Bean Queue qWorkflowDlq() { return QueueBuilder.durable("q.workflow.dlq").build(); }

    @Bean Binding bWorkflowCaixaAprovado() { return BindingBuilder.bind(qWorkflow()).to(sorvExchange()).with("caixa.pagamento.aprovado"); }
    @Bean Binding bWorkflowCaixaNegado() { return BindingBuilder.bind(qWorkflow()).to(sorvExchange()).with("caixa.pagamento.negado"); }
    @Bean Binding bWorkflowEstoqueConfirmada() { return BindingBuilder.bind(qWorkflow()).to(sorvExchange()).with("estoque.reserva.confirmada"); }
    @Bean Binding bWorkflowEstoqueNegada() { return BindingBuilder.bind(qWorkflow()).to(sorvExchange()).with("estoque.reserva.negada"); }
    @Bean Binding bWorkflowRetry() { return BindingBuilder.bind(qWorkflowRetry()).to(sorvExchange()).with("workflow.retry"); }
    @Bean Binding bWorkflowRetryBack() { return BindingBuilder.bind(qWorkflow()).to(sorvExchange()).with("workflow.retry.back"); }
    @Bean Binding bWorkflowDlq() { return BindingBuilder.bind(qWorkflowDlq()).to(sorvDlx()).with("workflow.dead"); }

    // ==================== CAIXA ====================
    @Bean
    Queue qCaixa() {
        return QueueBuilder.durable("q.caixa")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "caixa.dead")
            .build();
    }

    @Bean
    Queue qCaixaRetry() {
        return QueueBuilder.durable("q.caixa.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "caixa.retry.back")
            .build();
    }

    @Bean Queue qCaixaDlq() { return QueueBuilder.durable("q.caixa.dlq").build(); }

    @Bean Binding bCaixaIniciado() { return BindingBuilder.bind(qCaixa()).to(sorvExchange()).with("caixa.pagamento.iniciado"); }
    @Bean Binding bCaixaRetry() { return BindingBuilder.bind(qCaixaRetry()).to(sorvExchange()).with("caixa.retry"); }
    @Bean Binding bCaixaRetryBack() { return BindingBuilder.bind(qCaixa()).to(sorvExchange()).with("caixa.retry.back"); }
    @Bean Binding bCaixaDlq() { return BindingBuilder.bind(qCaixaDlq()).to(sorvDlx()).with("caixa.dead"); }

    // ==================== ESTOQUE ====================
    @Bean
    Queue qEstoque() {
        return QueueBuilder.durable("q.estoque")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "estoque.dead")
            .build();
    }

    @Bean
    Queue qEstoqueRetry() {
        return QueueBuilder.durable("q.estoque.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "estoque.retry.back")
            .build();
    }

    @Bean Queue qEstoqueDlq() { return QueueBuilder.durable("q.estoque.dlq").build(); }

    @Bean Binding bEstoqueSolicitada() { return BindingBuilder.bind(qEstoque()).to(sorvExchange()).with("estoque.reserva.solicitada"); }
    @Bean Binding bEstoqueRetry() { return BindingBuilder.bind(qEstoqueRetry()).to(sorvExchange()).with("estoque.retry"); }
    @Bean Binding bEstoqueRetryBack() { return BindingBuilder.bind(qEstoque()).to(sorvExchange()).with("estoque.retry.back"); }
    @Bean Binding bEstoqueDlq() { return BindingBuilder.bind(qEstoqueDlq()).to(sorvDlx()).with("estoque.dead"); }

    // ==================== PRODUÇÃO ====================
    @Bean
    Queue qProducao() {
        return QueueBuilder.durable("q.producao")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "producao.dead")
            .build();
    }

    @Bean
    Queue qProducaoRetry() {
        return QueueBuilder.durable("q.producao.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "producao.retry.back")
            .build();
    }

    @Bean Queue qProducaoDlq() { return QueueBuilder.durable("q.producao.dlq").build(); }

    @Bean Binding bProducaoConfirmado() { return BindingBuilder.bind(qProducao()).to(sorvExchange()).with("pedidos.pedido.confirmado"); }
    @Bean Binding bProducaoPronto() { return BindingBuilder.bind(qProducao()).to(sorvExchange()).with("pedidos.pedido.pronto"); }
    @Bean Binding bProducaoRetry() { return BindingBuilder.bind(qProducaoRetry()).to(sorvExchange()).with("producao.retry"); }
    @Bean Binding bProducaoRetryBack() { return BindingBuilder.bind(qProducao()).to(sorvExchange()).with("producao.retry.back"); }
    @Bean Binding bProducaoDlq() { return BindingBuilder.bind(qProducaoDlq()).to(sorvDlx()).with("producao.dead"); }

    // ==================== ENTREGAS ====================
    @Bean
    Queue qEntregas() {
        return QueueBuilder.durable("q.entregas")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "entregas.dead")
            .build();
    }

    @Bean
    Queue qEntregasRetry() {
        return QueueBuilder.durable("q.entregas.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "entregas.retry.back")
            .build();
    }

    @Bean Queue qEntregasDlq() { return QueueBuilder.durable("q.entregas.dlq").build(); }

    @Bean Binding bEntregasCriar() { return BindingBuilder.bind(qEntregas()).to(sorvExchange()).with("entregas.pedido.criar"); }
    @Bean Binding bEntregasDespachado() { return BindingBuilder.bind(qEntregas()).to(sorvExchange()).with("entregas.pedido.despachado"); }
    @Bean Binding bEntregasACaminho() { return BindingBuilder.bind(qEntregas()).to(sorvExchange()).with("entregas.pedido.a_caminho"); }
    @Bean Binding bEntregasEntregue() { return BindingBuilder.bind(qEntregas()).to(sorvExchange()).with("entregas.pedido.entregue"); }
    @Bean Binding bEntregasRetry() { return BindingBuilder.bind(qEntregasRetry()).to(sorvExchange()).with("entregas.retry"); }
    @Bean Binding bEntregasRetryBack() { return BindingBuilder.bind(qEntregas()).to(sorvExchange()).with("entregas.retry.back"); }
    @Bean Binding bEntregasDlq() { return BindingBuilder.bind(qEntregasDlq()).to(sorvDlx()).with("entregas.dead"); }

    // ==================== CLIENTE ====================
    @Bean
    Queue qCliente() {
        return QueueBuilder.durable("q.cliente")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "cliente.dead")
            .build();
    }

    @Bean
    Queue qClienteRetry() {
        return QueueBuilder.durable("q.cliente.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "cliente.retry.back")
            .build();
    }

    @Bean Queue qClienteDlq() { return QueueBuilder.durable("q.cliente.dlq").build(); }

    @Bean Binding bClientePronto() { return BindingBuilder.bind(qCliente()).to(sorvExchange()).with("cliente.pedido.pronto"); }
    @Bean Binding bClienteDespachado() { return BindingBuilder.bind(qCliente()).to(sorvExchange()).with("cliente.pedido.despachado"); }
    @Bean Binding bClienteACaminho() { return BindingBuilder.bind(qCliente()).to(sorvExchange()).with("cliente.pedido.a_caminho"); }
    @Bean Binding bClienteEntregue() { return BindingBuilder.bind(qCliente()).to(sorvExchange()).with("cliente.pedido.entregue"); }
    @Bean Binding bClienteRetry() { return BindingBuilder.bind(qClienteRetry()).to(sorvExchange()).with("cliente.retry"); }
    @Bean Binding bClienteRetryBack() { return BindingBuilder.bind(qCliente()).to(sorvExchange()).with("cliente.retry.back"); }
    @Bean Binding bClienteDlq() { return BindingBuilder.bind(qClienteDlq()).to(sorvDlx()).with("cliente.dead"); }

    // ==================== RELATÓRIO ====================
    @Bean
    Queue qRelatorio() {
        return QueueBuilder.durable("q.relatorio")
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", "relatorio.dead")
            .build();
    }

    @Bean
    Queue qRelatorioRetry() {
        return QueueBuilder.durable("q.relatorio.retry")
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", "relatorio.retry.back")
            .build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // Configurar mapeamento de tipos
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.IceCreamParlor.dto.events");

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean Queue qRelatorioDlq() { return QueueBuilder.durable("q.relatorio.dlq").build(); }

    @Bean Binding bRelatorioEvento() { return BindingBuilder.bind(qRelatorio()).to(sorvExchange()).with("relatorio.evento.recebido"); }
    @Bean Binding bRelatorioRetry() { return BindingBuilder.bind(qRelatorioRetry()).to(sorvExchange()).with("relatorio.retry"); }
    @Bean Binding bRelatorioRetryBack() { return BindingBuilder.bind(qRelatorio()).to(sorvExchange()).with("relatorio.retry.back"); }
    @Bean Binding bRelatorioDlq() { return BindingBuilder.bind(qRelatorioDlq()).to(sorvDlx()).with("relatorio.dead"); }
}