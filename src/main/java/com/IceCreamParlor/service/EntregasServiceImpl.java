package com.IceCreamParlor.service;

import com.IceCreamParlor.consumer.EntregasConsumer;
import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.dto.repositories.EntregaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EntregasServiceImpl {

    private final EntregaRepository entregaRepository;

    private final EntregasConsumer entregaProducer;

    public EntregasServiceImpl(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
        this.entregaProducer = entregaRepository();
    }

    public void criarEntrega(String pedidoId, String clienteId) {
        // Lógica para criar uma nova entrega
        EntregaEntity entrega = new EntregaEntity(pedidoId, "CRIADO");
        entregaRepository.save(entrega);

        System.out.println("Entrega criada para o pedido: " + pedidoId);
    }

    public void marcarComoDespachado(UUID pedidoId, String clienteId) {
      atualizarStatus(pedidoId, "DESPACHADO");
      entregaProducer.enviarPedidoDespachado(new EntregaEvents.PedidoDespachado(pedidoId, clienteId));
    }

    public void marcarComoACaminho(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, "A_CAMINHO");
        entregaProducer.enviarPedidoACaminho(new EntregaEvents.PedidoACaminho(pedidoId, clienteId));
    }

    public void marcarEntregue(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, "ENTREGUE");
        entregaProducer.enviarPedidoEntregue(new EntregaEvents.PedidoEntregue(pedidoId, clienteId));
    }

    private void atualizarStatus(UUID pedidoId, String status) {
        EntregaEntity entrega = entregaRepository.findById(UUID.fromString(pedidoId.toString()))
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada para o pedido: " + pedidoId));
        entrega.setStatus(status);
        entregaRepository.save(entrega);
        System.out.println("Entrega para o pedido " + pedidoId + " atualizada para o status: " + status);
    }

}
