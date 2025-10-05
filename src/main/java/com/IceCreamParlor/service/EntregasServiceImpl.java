package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.repositories.EntregaRepository;
import com.IceCreamParlor.producer.ClienteProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntregasServiceImpl {

    private final EntregaRepository entregaRepository;
    private final ClienteProducer clienteProducer;

    @Transactional
    public void processarEntrega(WorkflowEvents.EntregaCriada evento, String correlationId, String usuario) {
        log.info("🚚 Processando criação de entrega - pedidoId: {}", evento.pedidoId());

        // Cria entrega
        criarEntrega(evento.pedidoId(), evento.clienteId());

        // Despacha
        marcarComoDespachado(evento.pedidoId(), evento.clienteId(), correlationId, usuario);

        // A caminho
        marcarComoACaminho(evento.pedidoId(), evento.clienteId(), correlationId, usuario);

        // Entregue
        marcarEntregue(evento.pedidoId(), evento.clienteId(), correlationId, usuario);
    }

    @Transactional
    public void criarEntrega(UUID pedidoId, String clienteId) {
        EntregaEntity entrega = new EntregaEntity(pedidoId, StatusEntregaEnum.CRIADO);
        entregaRepository.save(entrega);
        log.info("✅ Entrega criada - pedidoId: {}", pedidoId);
    }

    private void marcarComoDespachado(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.DESPACHADO);

        ClienteEvents.PedidoDespachado evento =
            new ClienteEvents.PedidoDespachado(pedidoId, clienteId);
        clienteProducer.publishPedidoDespachado(evento, correlationId, usuario);

        log.info("📦 Pedido despachado - pedidoId: {}", pedidoId);
    }

    private void marcarComoACaminho(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.A_CAMINHO);

        ClienteEvents.PedidoACaminho evento =
            new ClienteEvents.PedidoACaminho(pedidoId, clienteId);
        clienteProducer.publishPedidoACaminho(evento, correlationId, usuario);

        log.info("🚗 Pedido a caminho - pedidoId: {}", pedidoId);
    }

    private void marcarEntregue(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.ENTREGUE);

        ClienteEvents.PedidoEntregue evento =
            new ClienteEvents.PedidoEntregue(pedidoId, clienteId);
        clienteProducer.publishPedidoEntregue(evento, correlationId, usuario);

        log.info("✅ Pedido entregue - pedidoId: {}", pedidoId);
    }

    private void atualizarStatus(UUID pedidoId, StatusEntregaEnum status) {
        EntregaEntity entrega = entregaRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada: " + pedidoId));

        entrega.atualizarStatus(status);
        entregaRepository.save(entrega);
    }
}