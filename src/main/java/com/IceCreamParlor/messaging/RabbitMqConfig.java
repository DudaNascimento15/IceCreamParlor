package com.IceCreamParlor.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // Nomes padronizados
    public static final String EXCHANGE_MAIN = "sorv.ex";
    public static final String EXCHANGE_DLX = "sorv.dlx";

    // Filas principais
    public static final String Q_WORKFLOW = "q.workflow";
    public static final String Q_CAIXA = "q.caixa";
    public static final String Q_ESTOQUE = "q.estoque";
    public static final String Q_PRODUCAO = "q.producao";
    public static final String Q_ENTREGAS = "q.entregas";
    public static final String Q_CLIENTE = "q.cliente";
    public static final String Q_RELATORIO = "q.relatorio";

    // Filas retry
    public static final String Q_WORKFLOW_RETRY = "q.workflow.retry";
    public static final String Q_CAIXA_RETRY = "q.caixa.retry";
    public static final String Q_ESTOQUE_RETRY = "q.estoque.retry";
    public static final String Q_PRODUCAO_RETRY = "q.producao.retry";
    public static final String Q_ENTREGAS_RETRY = "q.entregas.retry";
    public static final String Q_CLIENTE_RETRY = "q.cliente.retry";
    public static final String Q_RELATORIO_RETRY = "q.relatorio.retry";

    // Filas DLQ
    public static final String Q_WORKFLOW_DLQ = "q.workflow.dlq";
    public static final String Q_CAIXA_DLQ = "q.caixa.dlq";
    public static final String Q_ESTOQUE_DLQ = "q.estoque.dlq";
    public static final String Q_PRODUCAO_DLQ = "q.producao.dlq";
    public static final String Q_ENTREGAS_DLQ = "q.entregas.dlq";
    public static final String Q_CLIENTE_DLQ = "q.cliente.dlq";
    public static final String Q_RELATORIO_DLQ = "q.relatorio.dlq";

    // TTL padrão de retry (ajuste se quiser backoff exponencial criando várias retries)
    private static final int RETRY_TTL_MS = 15_000;

    // Exchanges
    @Bean
    TopicExchange sorvExchange() {
        return new TopicExchange(EXCHANGE_MAIN, true, false);
    }

    @Bean
    DirectExchange sorvDlx() {
        return new DirectExchange(EXCHANGE_DLX, true, false);
    }

    // ===== Helpers =====
    private static Queue mainQueue(String name) {
        // manda falhas para o DLX com routing-key "<servico>.dead"
        String deadRouting = name.replace("q.", "").replace(".retry", "").replace(".dlq", "") + ".dead";
        return QueueBuilder.durable(name)
            .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
            .withArgument("x-dead-letter-routing-key", deadRouting)
            .build();
    }

    private static Queue retryQueue(String name, String originalRoutingKey) {
        // Retry espera TTL e volta para o exchange principal com a mesma routing key
        return QueueBuilder.durable(name)
            .withArgument("x-message-ttl", RETRY_TTL_MS)
            .withArgument("x-dead-letter-exchange", EXCHANGE_MAIN)
            .withArgument("x-dead-letter-routing-key", originalRoutingKey)
            .build();
    }

    private static Queue dlq(String name) {
        return QueueBuilder.durable(name).build();
    }

    private static Binding bindMain(Queue q, TopicExchange ex, String routing) {
        return BindingBuilder.bind(q).to(ex).with(routing);
    }

    private static Binding bindDlq(Queue q, DirectExchange dlx, String deadRouting) {
        return BindingBuilder.bind(q).to(dlx).with(deadRouting);
    }

    // ===== Declaração por domínio =====

    // WORKFLOW
    @Bean
    Queue qWorkflow() {
        return mainQueue(Q_WORKFLOW);
    }

    @Bean
    Queue qWorkflowRetry() {
        return retryQueue(Q_WORKFLOW_RETRY, "workflow.*");
    }

    @Bean
    Queue qWorkflowDlq() {
        return dlq(Q_WORKFLOW_DLQ);
    }

    @Bean
    Binding bWorkflow(TopicExchange sorvExchange) {
        return bindMain(qWorkflow(), sorvExchange, "workflow.*");
    }

    @Bean
    Binding bWorkflowRetry(TopicExchange sorvExchange) {
        return bindMain(qWorkflowRetry(), sorvExchange, "workflow.retry");
    }

    @Bean
    Binding bWorkflowDlq(DirectExchange sorvDlx) {
        return bindDlq(qWorkflowDlq(), sorvDlx, "workflow.dead");
    }

    // CAIXA
    @Bean
    Queue qCaixa() {
        return mainQueue(Q_CAIXA);
    }

    @Bean
    Queue qCaixaRetry() {
        return retryQueue(Q_CAIXA_RETRY, "caixa.#");
    }

    @Bean
    Queue qCaixaDlq() {
        return dlq(Q_CAIXA_DLQ);
    }

    @Bean
    Binding bCaixa(TopicExchange ex) {
        return bindMain(qCaixa(), ex, "caixa.#");
    }

    @Bean
    Binding bCaixaRetry(TopicExchange ex) {
        return bindMain(qCaixaRetry(), ex, "caixa.retry");
    }

    @Bean
    Binding bCaixaDlq(DirectExchange dlx) {
        return bindDlq(qCaixaDlq(), dlx, "caixa.dead");
    }

    // ESTOQUE
    @Bean
    Queue qEstoque() {
        return mainQueue(Q_ESTOQUE);
    }

    @Bean
    Queue qEstoqueRetry() {
        return retryQueue(Q_ESTOQUE_RETRY, "estoque.#");
    }

    @Bean
    Queue qEstoqueDlq() {
        return dlq(Q_ESTOQUE_DLQ);
    }

    @Bean
    Binding bEstoque(TopicExchange ex) {
        return bindMain(qEstoque(), ex, "estoque.#");
    }

    @Bean
    Binding bEstoqueRetry(TopicExchange ex) {
        return bindMain(qEstoqueRetry(), ex, "estoque.retry");
    }

    @Bean
    Binding bEstoqueDlq(DirectExchange dlx) {
        return bindDlq(qEstoqueDlq(), dlx, "estoque.dead");
    }

    // PRODUCAO
    @Bean
    Queue qProducao() {
        return mainQueue(Q_PRODUCAO);
    }

    @Bean
    Queue qProducaoRetry() {
        return retryQueue(Q_PRODUCAO_RETRY, "producao.#");
    }

    @Bean
    Queue qProducaoDlq() {
        return dlq(Q_PRODUCAO_DLQ);
    }

    @Bean
    Binding bProducao(TopicExchange ex) {
        return bindMain(qProducao(), ex, "producao.#");
    }

    @Bean
    Binding bProducaoRetry(TopicExchange ex) {
        return bindMain(qProducaoRetry(), ex, "producao.retry");
    }

    @Bean
    Binding bProducaoDlq(DirectExchange dlx) {
        return bindDlq(qProducaoDlq(), dlx, "producao.dead");
    }

    // ENTREGAS
    @Bean
    Queue qEntregas() {
        return mainQueue(Q_ENTREGAS);
    }

    @Bean
    Queue qEntregasRetry() {
        return retryQueue(Q_ENTREGAS_RETRY, "entregas.#");
    }

    @Bean
    Queue qEntregasDlq() {
        return dlq(Q_ENTREGAS_DLQ);
    }

    @Bean
    Binding bEntregas(TopicExchange ex) {
        return bindMain(qEntregas(), ex, "entregas.#");
    }

    @Bean
    Binding bEntregasRetry(TopicExchange ex) {
        return bindMain(qEntregasRetry(), ex, "entregas.retry");
    }

    @Bean
    Binding bEntregasDlq(DirectExchange dlx) {
        return bindDlq(qEntregasDlq(), dlx, "entregas.dead");
    }

    // CLIENTE
    @Bean
    Queue qCliente() {
        return mainQueue(Q_CLIENTE);
    }

    @Bean
    Queue qClienteRetry() {
        return retryQueue(Q_CLIENTE_RETRY, "cliente.#");
    }

    @Bean
    Queue qClienteDlq() {
        return dlq(Q_CLIENTE_DLQ);
    }

    @Bean
    Binding bCliente(TopicExchange ex) {
        return bindMain(qCliente(), ex, "cliente.#");
    }

    @Bean
    Binding bClienteRetry(TopicExchange ex) {
        return bindMain(qClienteRetry(), ex, "cliente.retry");
    }

    @Bean
    Binding bClienteDlq(DirectExchange dlx) {
        return bindDlq(qClienteDlq(), dlx, "cliente.dead");
    }

    // RELATORIO
    @Bean
    Queue qRelatorio() {
        return mainQueue(Q_RELATORIO);
    }

    @Bean
    Queue qRelatorioRetry() {
        return retryQueue(Q_RELATORIO_RETRY, "relatorio.#");
    }

    @Bean
    Queue qRelatorioDlq() {
        return dlq(Q_RELATORIO_DLQ);
    }

    @Bean
    Binding bRelatorio(TopicExchange ex) {
        return bindMain(qRelatorio(), ex, "relatorio.#");
    }

    @Bean
    Binding bRelatorioRetry(TopicExchange ex) {
        return bindMain(qRelatorioRetry(), ex, "relatorio.retry");
    }

    @Bean
    Binding bRelatorioDlq(DirectExchange dlx) {
        return bindDlq(qRelatorioDlq(), dlx, "relatorio.dead");
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(conv);
        return t;
    }

}
