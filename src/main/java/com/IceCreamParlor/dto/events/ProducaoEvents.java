package com.IceCreamParlor.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

public class ProducaoEvents {

    public record PedidoPronto(UUID pedidoId, String clienteId) {}

    public record PedidoConfirmado(UUID pedidoId, String clienteId) {}
}
