package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.dto.repositories.EntregaRepository;
import com.IceCreamParlor.producer.EntregasProducer;
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

    private final EntregasProducer entregasProducer;

    @Transactional
    public void processarEntrega(EntregaEvents.CriarEntrega evento, String correlationId, String usuario) {
        log.info("Processando criação de entrega - pedidoId: {}", evento.pedidoId());

        criarEntrega(evento.pedidoId(), evento.clienteId());

        marcarComoDespachado(evento.pedidoId(), evento.clienteId(), correlationId, usuario);

        marcarComoACaminho(evento.pedidoId(), evento.clienteId(), correlationId, usuario);

        marcarEntregue(evento.pedidoId(), evento.clienteId(), correlationId, usuario);
    }

      public void criarEntrega(UUID pedidoId, String clienteId) {
        EntregaEntity entrega = new EntregaEntity(pedidoId, StatusEntregaEnum.CRIADO);
        entregaRepository.save(entrega);
        log.info("entrega criada - pedidoId: {}", pedidoId);
    }

    private void marcarComoDespachado(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.DESPACHADO);

        EntregaEvents.PedidoDespachado evento =
            new EntregaEvents.PedidoDespachado(pedidoId, clienteId);
        entregasProducer.publishPedidoDespachado(evento, correlationId, usuario);

        log.info("Pedido despachado - pedidoId: {}", pedidoId);
    }

    private void marcarComoACaminho(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.A_CAMINHO);

        EntregaEvents.PedidoACaminho evento =
            new EntregaEvents.PedidoACaminho(pedidoId, clienteId);
        entregasProducer.publishPedidoACaminho(evento, correlationId, usuario);

        log.info("Pedido a caminho - pedidoId: {}", pedidoId);
    }

    private void marcarEntregue(UUID pedidoId, String clienteId, String correlationId, String usuario) {
        atualizarStatus(pedidoId, StatusEntregaEnum.ENTREGUE);

        EntregaEvents.PedidoEntregue evento =
            new EntregaEvents.PedidoEntregue(pedidoId, clienteId);
        entregasProducer.publishPedidoEntregue(evento, correlationId, usuario);

        log.info("Pedido entregue - pedidoId: {}", pedidoId);
    }

    private void atualizarStatus(UUID pedidoId, StatusEntregaEnum status) {
        EntregaEntity entrega = entregaRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada: " + pedidoId));

        entrega.atualizarStatus(status);
        entregaRepository.save(entrega);
    }

}
