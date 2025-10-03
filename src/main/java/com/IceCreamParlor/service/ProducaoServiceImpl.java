package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.dto.repositories.ProducaoRepository;
import com.icecreamparlor.producao.producer.ProducaoProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProducaoService {

    private final ProducaoRepository repository;
    private final ProducaoProducer producer;

    public ProducaoService(ProducaoRepository repository, ProducaoProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    /**
     * Inicia produção com status EM_PREPARO
     */
    public void iniciarProducao(ProducaoEvents.PedidoConfirmado evento) {
        ProducaoEntity producao = new ProducaoEntity(evento.pedidoId(), "EM_PREPARO");
        repository.save(producao);
        System.out.println("Produção iniciada para pedido " + evento.pedidoId());
    }

    /**
     * Finaliza produção com status PRONTO e envia notificação
     */
    public void finalizarProducao(UUID pedidoId, String clienteId) {
        ProducaoEntity producao = repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Produção não encontrada para pedido: " + pedidoId));

        producao.finalizarProducao();
        repository.save(producao);

        producer.enviarPedidoPronto(new ProducaoEvents.PedidoPronto(pedidoId, clienteId));
        System.out.println("Pedido pronto! Enviado para o Cliente: " + clienteId);
    }
}
