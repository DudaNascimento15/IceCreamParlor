package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.WorkflowEntity;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.dto.repositories.SagaStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        log.info("Iniciando pedido - pedidoId: {}, cliente: {}, valor: {}",
            pedidoId, clienteId, total);

        var s = new WorkflowEntity(
            pedidoId,
            clienteId,
            total
        );
        sagaStateRepository.save(s);

        var pedioIdStr = pedidoId.toString();

        publish
            ("caixa.pagamento.iniciado", new WorkflowEvents.PagamentoIniciado(pedidoId, usuario, total), pedioIdStr, usuario);

        publish("estoque.reserva.solicitada", new WorkflowEvents.ReservaSolicitada(pedidoId, usuario), pedioIdStr, usuario);

        publish("entrega.pedido.criar", new WorkflowEvents.EntregaCriada(pedidoId, s.getClienteId()), pedioIdStr, usuario);

        log.info("Pedido Criado! Enviado para o Cliente {} , pedido {} : " + usuario, pedioIdStr);

        return s.getPedidoId();
    }

    ;

    @Transactional
    public void pagamentoNegado(UUID pedidoId) {
        log.warn(" Pagamento negado - pedidoId: {}", pedidoId);
        WorkflowEntity workflow = sagaStateRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Workflow não encontrado: " + pedidoId));
    }

    ;

    @Transactional
    public void pagamentoAprovado(UUID pedidoId, String usuario) {
        log.warn(" Pagamento aprovado - pedidoId: {}", pedidoId);
        var s = sagaStateRepository.findById(pedidoId).orElseThrow();
        s.setPagamentoAprovado(true);
        sagaStateRepository.save(s);
        tentaConfirmar(s, usuario);
    }

    ;


    @Transactional
    public void reservaConfirmada(UUID pedidoId, String usuario) {
        log.info("Reserva confirmada - pedidoId: {}", pedidoId);

        var s = sagaStateRepository.findById(pedidoId).orElseThrow();
        s.setEstoqueReservado(true);
        sagaStateRepository.save(s);
        tentaConfirmar(s, usuario);
    }

    ;

    public void reservaNegada(UUID pedidoId) {
        log.warn("Reserva negada - pedidoId: {}", pedidoId);
        sagaStateRepository.findById(pedidoId).orElseThrow();
    }

    ;

    public void tentaConfirmar(WorkflowEntity workflowEntity, String usuario) {
        if (workflowEntity.isPagamentoAprovado() && workflowEntity.isEstoqueReservado()) {
            publish("pedidos.pedido.confirmado",
                new WorkflowEvents.PedidoConfirmado(
                    workflowEntity.getPedidoId(),
                    workflowEntity.getClienteId(),
                    workflowEntity.getValorTotal()
                ), workflowEntity.getPedidoId().toString(), usuario);

        } else {
            log.debug("⏳ Aguardando confirmações - pedidoId: {}, pagamento: {}, estoque: {}",
                workflowEntity.getPedidoId(),
                workflowEntity.isPagamentoAprovado(),
                workflowEntity.isEstoqueReservado());
        }
    }

    ;

    public void publish(String routingKey, Object payload, String correlationId, String usuario) {
        rabbit.convertAndSend(EX, routingKey, payload, msg -> {
            msg.getMessageProperties().setContentType("aplication/json");
            msg.getMessageProperties().setCorrelationId(correlationId);
            msg.getMessageProperties().setHeader("x-user", usuario);
            msg.getMessageProperties().setHeader("x-event-type", routingKey);
            return msg;
        });
        log.debug("Mensagem publicada - routing: {}, correlationId: {}", routingKey, correlationId);
    }
    }

    ;

}
