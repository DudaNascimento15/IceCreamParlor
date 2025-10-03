package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import com.IceCreamParlor.dto.enums.StatusEntregaEnum;
import com.IceCreamParlor.dto.events.EntregaEvents;
import com.IceCreamParlor.dto.repositories.EntregaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.internal.util.PessimisticNumberParser;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.IceCreamParlor.dto.enums.StatusEntregaEnum.DESPACHADO;

@Service
@RequiredArgsConstructor
public class EntregasServiceImpl {

    private final EntregaRepository entregaRepository;

    private final EntregaProducer entregaProducer;


    public void criarEntrega(String pedidoId, String clienteId) {
        // Lógica para criar uma nova entrega
        EntregaEntity entrega = new EntregaEntity(pedidoId, StatusEntregaEnum.CRIADO.toString());
        entregaRepository.save(entrega);

        System.out.println("Entrega criada para o pedido: " + pedidoId);
    }

    public void marcarComoDespachado(UUID pedidoId, String clienteId) {
      atualizarStatus(pedidoId, StatusEntregaEnum.DESPACHADO.toString());
      entregaProducer.enviarPedidoDespachado(new EntregaEvents.PedidoDespachado(pedidoId, clienteId));
    }

    public void marcarComoACaminho(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, StatusEntregaEnum.A_CAMINHO.toString());
        entregaProducer.enviarPedidoACaminho(new EntregaEvents.PedidoACaminho(pedidoId, clienteId));
    }

    public void marcarEntregue(UUID pedidoId, String clienteId) {
        atualizarStatus(pedidoId, StatusEntregaEnum.ENTREGUE.toString());
        entregaProducer.enviarPedidoEntregue(new EntregaEvents.PedidoEntregue(pedidoId, clienteId));
    }

    private void atualizarStatus(UUID pedidoId, String status) {
        EntregaEntity entrega = entregaRepository.findById(UUID.fromString(pedidoId.toString()))
            .orElseThrow(() -> new RuntimeException("Entrega não encontrada para o pedido: " + pedidoId));
        entrega.setStatus(StatusEntregaEnum.valueOf(status));
        entregaRepository.save(entrega);
        System.out.println("Entrega para o pedido " + pedidoId + " atualizada para o status: " + status);
    }

}
