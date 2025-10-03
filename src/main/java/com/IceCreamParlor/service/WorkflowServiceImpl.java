package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.WorkflowEntity;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.dto.repositories.SagaStateRepository;
import com.IceCreamParlor.service.insterfaces.WorkflowService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final SagaStateRepository sagaStateRepository;

    private final RabbitTemplate rabbit;

    public WorkflowServiceImpl(SagaStateRepository sagaStateRepository, RabbitTemplate rabbitTemplate) {
        this.sagaStateRepository = sagaStateRepository;
        this.rabbit = rabbitTemplate;
    }

    private static final String EX = "sorv.ex";

    @Transactional
    public UUID iniciarPedido(String clienteId, BigDecimal total, String usuario) {
        UUID pedidoId = UUID.randomUUID();

        var s = new WorkflowEntity(pedidoId, clienteId, total);
        sagaStateRepository.save(s);

        var pedioIdStr = pedidoId.toString();

        publish
            ("caixa.pagamento.iniciado", new WorkflowEvents.PagamentoIniciado(pedidoId, usuario, total), pedioIdStr, usuario);

        publish("estoque.reserva.solicitada", new WorkflowEvents.ReservaSolicitada(pedidoId,usuario), pedioIdStr, usuario);

        publish("entrega.pedido.criar", new WorkflowEvents.EntregaCriada(pedidoId, s.getClienteId()), pedioIdStr, usuario);

        return s.getPedidoId();
    }

    ;

    public void pagamentoNegado(UUID pedidoId) {
        sagaStateRepository.findById(pedidoId).orElseThrow();
    }

    ;

    public void pagamentoAprovado(UUID pedidoId, String usuario) {
        var s = sagaStateRepository.findById(pedidoId).orElseThrow();
        s.setPagamentoAprovado(true);

        tentaConfirmar(s, usuario);
    }

    ;


    public void reservaConfirmada(UUID pedidoId, String usuario) {
        var s = sagaStateRepository.findById(pedidoId).orElseThrow();
        s.setEstoqueReservado(true);
        tentaConfirmar(s, usuario);
    }

    ;

    public void reservaNegada(UUID pedidoId) {
        sagaStateRepository.findById(pedidoId).orElseThrow();
    }

    ;

    public void tentaConfirmar(WorkflowEntity workflowEntity, String usuario){
        if (workflowEntity.isPagamentoAprovado() && workflowEntity.isEstoqueReservado()) {
           publish("pedidos.pedido.confirmado",
               new WorkflowEvents.PedidoConfirmado(
                   workflowEntity.getPedidoId(),
                   workflowEntity.getClienteId(),
                   workflowEntity.getValorTotal()
               ), workflowEntity.getPedidoId().toString(), usuario);

        }
    };

    public void publish(String routingKey, Object payload, String correlationId, String usuario){
        rabbit.convertAndSend(EX, routingKey, payload, msg  ->{
            msg.getMessageProperties().setContentType("aplication/json");
            msg.getMessageProperties().setCorrelationId(correlationId);
            msg.getMessageProperties().setHeader("x-user", usuario);
            msg.getMessageProperties().setHeader("x-event-type", routingKey);
            return msg;
        });
    };

}
