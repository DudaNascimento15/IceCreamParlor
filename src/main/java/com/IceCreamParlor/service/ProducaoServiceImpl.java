package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.enums.StatusProducaoEnum;
import com.IceCreamParlor.dto.events.ClienteEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.repositories.ProducaoRepository;
import com.IceCreamParlor.producer.ClienteProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducaoServiceImpl {

    private final ProducaoRepository repository;
    private final ClienteProducer clienteProducer;

    @Transactional
    public void iniciarProducao(WorkflowEvents.PedidoConfirmado evento, String correlationId, String usuario) {
        log.info("🏭 Iniciando produção para o pedido: {}", evento.pedidoId());

        ProducaoEntity producao = new ProducaoEntity(
            evento.pedidoId(),
            StatusProducaoEnum.EM_PREPARO.toString()
        );
        repository.save(producao);

        log.info("✅ Produção iniciada para pedido {}", evento.pedidoId());

        // Finaliza produção passando a entidade já criada
        finalizarProducao(producao, evento.clienteId(), usuario);
    }

    @Transactional
    public void finalizarProducao(ProducaoEntity producao, String clienteId, String usuario) {
        log.info("🎉 Finalizando produção para o pedido: {}", producao.getPedidoId());

        producao.finalizarProducao();
        repository.save(producao);

        // Publica evento de pedido pronto para cliente
        ClienteEvents.PedidoPronto pedidoProntoCliente =
            new ClienteEvents.PedidoPronto(producao.getPedidoId(), clienteId);
        clienteProducer.publishPedidoPronto(pedidoProntoCliente, producao.getPedidoId().toString(), usuario);

        log.info("✅ Pedido pronto! Notificação enviada para o cliente: {}", clienteId);
    }
}