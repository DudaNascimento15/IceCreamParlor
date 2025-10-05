package com.IceCreamParlor.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestPublishController {

    private static final String EXCHANGE = "sorv.ex";
    private final RabbitTemplate rabbitTemplate;

    public TestPublishController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @PostMapping("/caixa")
    public ResponseEntity<String> publishCaixa(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "workflow.process", payload);
        return ResponseEntity.ok("Mensagem enviada para CaixaConsumer (workflow.process)");
    }


    @PostMapping("/producao")
    public ResponseEntity<String> publishProducao(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "estoque.process", payload);
        return ResponseEntity.ok("Mensagem enviada para ProducaoConsumer (estoque.process)");
    }


    @PostMapping("/entregas")
    public ResponseEntity<String> publishEntregas(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "producao.process", payload);
        return ResponseEntity.ok("Mensagem enviada para EntregasConsumer (producao.process)");
    }


    @PostMapping("/cliente")
    public ResponseEntity<String> publishCliente(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "entregas.process", payload);
        return ResponseEntity.ok("Mensagem enviada para ClienteConsumer (entregas.process)");
    }


    @PostMapping("/relatorio")
    public ResponseEntity<String> publishRelatorio(@RequestBody Map<String, Object> payload) {
        rabbitTemplate.convertAndSend(EXCHANGE, "cliente.process", payload);
        return ResponseEntity.ok("Mensagem enviada para RelatorioConsumer (cliente.process)");
    }
}
