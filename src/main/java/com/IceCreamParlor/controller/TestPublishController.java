package com.IceCreamParlor.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestPublishController {

    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "sorv.ex";

    public TestPublishController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // ========== TESTES INDIVIDUAIS ==========

    @PostMapping("/caixa")
    public ResponseEntity<String> sendCaixa(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "workflow.process", payload);
        return ResponseEntity.ok("Mensagem enviada para CaixaConsumer (workflow.process)");
    }

    @PostMapping("/estoque")
    public ResponseEntity<String> sendEstoque(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "caixa.pagamento.aprovado", payload);
        return ResponseEntity.ok("Mensagem enviada para EstoqueConsumer (caixa.pagamento.aprovado)");
    }

    @PostMapping("/producao")
    public ResponseEntity<String> sendProducao(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "estoque.reserva.confirmada", payload);
        return ResponseEntity.ok("Mensagem enviada para ProducaoConsumer (estoque.reserva.confirmada)");
    }

    @PostMapping("/entregas")
    public ResponseEntity<String> sendEntregas(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "producao.concluida", payload);
        return ResponseEntity.ok("Mensagem enviada para EntregasConsumer (producao.concluida)");
    }

    @PostMapping("/cliente")
    public ResponseEntity<String> sendCliente(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "entregas.pedido.entregue", payload);
        return ResponseEntity.ok("Mensagem enviada para ClienteConsumer (entregas.pedido.entregue)");
    }

    @PostMapping("/relatorio")
    public ResponseEntity<String> sendRelatorio(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.notificacao.enviada", payload);
        return ResponseEntity.ok("Mensagem enviada para RelatorioConsumer (cliente.notificacao.enviada)");
    }

    // ========== TESTE DE FLUXO COMPLETO ==========

    @PostMapping("/fluxoCompleto")
    public ResponseEntity<String> fullFlow(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "workflow.process", payload);              // Workflow → Caixa
        rabbitTemplate.convertAndSend(EXCHANGE, "caixa.pagamento.aprovado", payload);       // Caixa → Estoque
        rabbitTemplate.convertAndSend(EXCHANGE, "estoque.reserva.confirmada", payload);     // Estoque → Produção
        rabbitTemplate.convertAndSend(EXCHANGE, "producao.concluida", payload);             // Produção → Entregas
        rabbitTemplate.convertAndSend(EXCHANGE, "entregas.pedido.entregue", payload);       // Entregas → Cliente
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.notificacao.enviada", payload);    // Cliente → Relatório
        return ResponseEntity.ok("✅ Fluxo completo executado!");
    }
}
