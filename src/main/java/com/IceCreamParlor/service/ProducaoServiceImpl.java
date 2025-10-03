package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import com.IceCreamParlor.dto.enums.StatusProducaoEnum;
import com.IceCreamParlor.dto.events.ProducaoEvents;
import com.IceCreamParlor.dto.repositories.ProducaoRepository;
import com.IceCreamParlor.producer.ProducaoProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProducaoServiceImpl {

    private final ProducaoRepository repository;

    private final ProducaoProducer producer;

    public void iniciarProducao(ProducaoEvents.PedidoConfirmado evento) {
        ProducaoEntity producao = new ProducaoEntity(evento.pedidoId(), StatusProducaoEnum.EM_PREPARO.toString());
        repository.save(producao);
        System.out.println("Produção iniciada para pedido " + evento.pedidoId());
    }

    public void finalizarProducao(UUID pedidoId, String clienteId) {
        ProducaoEntity producao = repository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Produção não encontrada para pedido: " + pedidoId));

        producao.finalizarProducao();
        repository.save(producao);

        producer.publishPedidoPronto(new ProducaoEvents.PedidoPronto(pedidoId, clienteId), pedidoId.toString(), clienteId);
        System.out.println("Pedido pronto! Enviado para o Cliente: " + clienteId);
    }
}
