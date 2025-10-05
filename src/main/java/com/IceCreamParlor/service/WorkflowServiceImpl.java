package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.WorkflowEntity;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.repositories.SagaStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl {

    private final SagaStateRepository sagaStateRepository;
    private final RabbitTemplate rabbit;
    private static final String EX = "sorv.ex";

    @Transactional
    public UUID iniciarPedido(String clienteId, BigDecimal total, String usuario) {
        UUID pedidoId = UUID.randomUUID();

        log.info("🚀 ========== INICIANDO PEDIDO ==========");
        log.info("🚀 pedidoId: {}", pedidoId);
        log.info("🚀 cliente: {}", clienteId);
        log.info("🚀 valor: {}", total);
        log.info("🚀 usuario: {}", usuario);

        WorkflowEntity workflow = new WorkflowEntity(pedidoId, clienteId, total);
        sagaStateRepository.save(workflow);
        log.info("✅ Workflow salvo no banco!");

        String pedidoIdStr = pedidoId.toString();

        log.info("📤 Publicando evento: caixa.pagamento.iniciado");
        publish("caixa.pagamento.iniciado",
            new WorkflowEvents.PagamentoIniciado(pedidoId, clienteId, total),
            pedidoIdStr, usuario);

        log.info("📤 Publicando evento: estoque.reserva.solicitada");
        publish("estoque.reserva.solicitada",
            new WorkflowEvents.ReservaSolicitada(pedidoId, clienteId),
            pedidoIdStr, usuario);

        log.info("✅ Pedido criado com sucesso! pedidoId: {}", pedidoId);
        log.info("🚀 ========== FIM INICIANDO PEDIDO ==========");

        return pedidoId;
    }

    @Transactional
    public void pagamentoAprovado(UUID pedidoId, String usuario) {
        log.info("💰 Pagamento aprovado - pedidoId: {}", pedidoId);

        WorkflowEntity workflow = sagaStateRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Workflow não encontrado: " + pedidoId));

        workflow.setPagamentoOk(true);
        workflow.setStatus("PAGAMENTO_OK");
        sagaStateRepository.save(workflow);

        tentaConfirmar(workflow, usuario);
    }

    @Transactional
    public void pagamentoNegado(UUID pedidoId) {
        log.warn("❌ Pagamento negado - pedidoId: {}", pedidoId);

        WorkflowEntity workflow = sagaStateRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Workflow não encontrado: " + pedidoId));

        workflow.setStatus("PAGAMENTO_NEGADO");
        sagaStateRepository.save(workflow);
    }

    @Transactional
    public void reservaConfirmada(UUID pedidoId, String usuario) {
        log.info("📦 Reserva confirmada - pedidoId: {}", pedidoId);

        WorkflowEntity workflow = sagaStateRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Workflow não encontrado: " + pedidoId));

        workflow.setEstoqueOk(true);
        workflow.setStatus("ESTOQUE_OK");
        sagaStateRepository.save(workflow);

        tentaConfirmar(workflow, usuario);
    }

    @Transactional
    public void reservaNegada(UUID pedidoId) {
        log.warn("❌ Reserva negada - pedidoId: {}", pedidoId);

        WorkflowEntity workflow = sagaStateRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Workflow não encontrado: " + pedidoId));

        workflow.setStatus("ESTOQUE_NEGADO");
        sagaStateRepository.save(workflow);
    }

    private void tentaConfirmar(WorkflowEntity workflow, String usuario) {
        if (Boolean.TRUE.equals(workflow.getPagamentoOk()) && Boolean.TRUE.equals(workflow.getEstoqueOk())) {
            log.info("✅ CONFIRMANDO PEDIDO! - pedidoId: {}", workflow.getPedidoId());

            workflow.setStatus("CONFIRMADO");
            workflow.setConfirmadoEm(OffsetDateTime.now());
            sagaStateRepository.save(workflow);

            log.info("📤 PUBLICANDO pedidos.pedido.confirmado");
            publish("pedidos.pedido.confirmado",
                new WorkflowEvents.PedidoConfirmado(
                    workflow.getPedidoId(),
                    workflow.getClienteId(),
                    workflow.getTotal()
                ),
                workflow.getPedidoId().toString(),
                usuario);

            log.info("📤 PUBLICANDO entregas.pedido.criar");
            publish("entregas.pedido.criar",
                new WorkflowEvents.EntregaCriada(
                    workflow.getPedidoId(),
                    workflow.getClienteId()
                ),
                workflow.getPedidoId().toString(),
                usuario);

            log.info("✅ EVENTOS PUBLICADOS COM SUCESSO!");

        } else {
            log.warn("⏳ AGUARDANDO - pagamento: {}, estoque: {}",
                workflow.getPagamentoOk(), workflow.getEstoqueOk());
        }
    }

    private void publish(String routingKey, Object payload, String correlationId, String usuario) {
        rabbit.convertAndSend(EX, routingKey, payload, msg -> {
            msg.getMessageProperties().setContentType("application/json");
            msg.getMessageProperties().setCorrelationId(correlationId);
            msg.getMessageProperties().setHeader("x-user", usuario);
            msg.getMessageProperties().setHeader("x-event-type", routingKey);
            return msg;
        });
        log.debug("📤 Mensagem publicada - routing: {}, correlationId: {}", routingKey, correlationId);
    }
}