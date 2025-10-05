package com.IceCreamParlor.consumer;

import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.service.ClienteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClienteConsumer {

    private final ClienteServiceImpl clienteService;
    private final ObjectMapper objectMapper;

    public ClienteConsumer(ClienteServiceImpl clienteService, ObjectMapper objectMapper) {
        this.clienteService = clienteService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "q.cliente", containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();

            log.info("👤 ClienteConsumer - routingKey: {}", routingKey);

            if ("cliente.pedido.pronto".equals(routingKey)) {
                ClienteEvents.PedidoPronto evento = objectMapper.readValue(
                    message.getBody(),
                    ClienteEvents.PedidoPronto.class
                );
                log.info("🎉 Notificando cliente - pedido pronto: {}", evento.pedidoId());
                clienteService.notificarPedidoPronto(evento);

            } else if ("cliente.pedido.despachado".equals(routingKey)) {
                ClienteEvents.PedidoDespachado evento = objectMapper.readValue(
                    message.getBody(),
                    ClienteEvents.PedidoDespachado.class
                );
                log.info("📦 Notificando cliente - pedido despachado: {}", evento.pedidoId());
                clienteService.notificarPedidoDespachado(evento);

            } else if ("cliente.pedido.a_caminho".equals(routingKey)) {
                ClienteEvents.PedidoACaminho evento = objectMapper.readValue(
                    message.getBody(),
                    ClienteEvents.PedidoACaminho.class
                );
                log.info("🚗 Notificando cliente - pedido a caminho: {}", evento.pedidoId());
                clienteService.notificarPedidoACaminho(evento);

            } else if ("cliente.pedido.entregue".equals(routingKey)) {
                ClienteEvents.PedidoEntregue evento = objectMapper.readValue(
                    message.getBody(),
                    ClienteEvents.PedidoEntregue.class
                );
                log.info("✅ Notificando cliente - pedido entregue: {}", evento.pedidoId());
                clienteService.notificarPedidoEntregue(evento);
            }
        } catch (Exception e) {
            log.error("❌ Erro no ClienteConsumer: {}", e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Falha no processamento", e);
        }
    }

    @RabbitListener(queues = "q.cliente.dlq", containerFactory = "rabbitListenerContainerFactory")
    public void handleDlq(Message message) {
        log.warn("⚠️ Mensagem em DLQ de Cliente: {}", new String(message.getBody()));
    }
}