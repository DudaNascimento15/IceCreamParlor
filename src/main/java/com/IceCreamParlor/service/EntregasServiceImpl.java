package com.IceCreamParlor.service;

import com.IceCreamParlor.consumer.EntregasConsumer;
import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.dto.repositories.EntregaRepository;
import com.IceCreamParlor.producer.EntregasProducer;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.internal.util.PessimisticNumberParser;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.IceCreamParlor.dto.enums.StatusEntregaEnum.DESPACHADO;

@Service
@RequiredArgsConstructor
public class EntregasServiceImpl {

    private final EntregaRepository entregaRepository;

    private final EntregasConsumer entregaProducer;

    private final EntregasProducer entregasProducer;


    public void criarEntrega(String pedidoId, String clienteId) {
        // Lógica para criar uma nova entrega
        EntregaEntity entrega = new EntregaEntity(pedidoId, StatusEntregaEnum.CRIADO.toString());
        entregaRepository.save(entrega);

        System.out.println("Entrega criada para o pedido: " + pedidoId);
    }

    public void marcarComoDespachado(UUID pedidoId, String clienteId) {
      atualizarStatus(pedidoId, StatusEntregaEnum.DESPACHADO.toString());
        entregasProducer.publishPedidoDespachado(new EntregaEvents.PedidoDespachado(pedidoId, clienteId), pedidoId.toString(), clienteId);
    }

    public void marcarComoACaminho(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, StatusEntregaEnum.A_CAMINHO.toString());
        entregasProducer.publishPedidoACaminho(new EntregaEvents.PedidoACaminho(pedidoId, clienteId), pedidoId.toString(), clienteId);
    }

    public void marcarEntregue(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, StatusEntregaEnum.ENTREGUE.toString());
        entregasProducer.publishPedidoEntregue(new EntregaEvents.PedidoEntregue(pedidoId, clienteId), pedidoId.toString(), clienteId);
    }

    private void atualizarStatus(UUID pedidoId, String status) {
        EntregaEntity entrega = entregaRepository.findById(UUID.fromString(pedidoId.toString()))
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada para o pedido: " + pedidoId));
        entrega.setStatus(StatusEntregaEnum.valueOf(status));
        entregaRepository.save(entrega);
        System.out.println("Entrega para o pedido " + pedidoId + " atualizada para o status: " + status);
    }

}
