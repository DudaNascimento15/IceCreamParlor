package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.enums.StatusProducaoEnum;
import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.dto.repositories.ProducaoRepository;
import com.IceCreamParlor.producer.ProducaoProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProducaoServiceImpl {

    private final ProducaoRepository repository;

    private final ProducaoProducer producer;

    public void iniciarProducao(WorkflowEvents.PedidoConfirmado evento, String correlationId, String usuario) {
        log.info("Iniciando produção para o pedido: " + evento.pedidoId());

        ProducaoEntity producao =
            new ProducaoEntity(
                evento.pedidoId(),
                StatusProducaoEnum.EM_PREPARO
            );

        repository.save(producao);
        log.info("Produção iniciada para pedido " + evento.pedidoId());
        finalizarProducao(evento.pedidoId(), evento.clienteId());
    }

    public void finalizarProducao(UUID pedidoId, String clienteId) {
        log.info("Finalizado produção para o pedido: " + pedidoId);

        ProducaoEntity producao = repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Produção não encontrada para pedido: " + pedidoId));

        producao.finalizarProducao();
        repository.save(producao);

        ProducaoEvents.PedidoPronto prontoEvent =
            new ProducaoEvents.PedidoPronto(pedidoId, clienteId);
        producer.publishPedidoPronto(prontoEvent, pedidoId.toString(), clienteId);
        log.info("Pedido pronto! Enviado para o Cliente: " + clienteId);
    }
}
