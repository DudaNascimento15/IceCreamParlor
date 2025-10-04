package com.IceCreamParlor.dto.events;

import java.util.UUID;

public class EntregaEvents {



    public record PedidoDespachado(UUID pedidoId, String clienteId) {
    }

    public record PedidoACaminho(UUID pedidoId, String clienteId) {
    }

    public record PedidoEntregue(UUID pedidoId, String clienteId) {
    }


}
