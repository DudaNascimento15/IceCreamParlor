package com.IceCreamParlor.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkflowEvents {

    public record PagamentoIniciado(UUID pedidoId, String clienteId, BigDecimal valorTotal){};

    public record ReservaSolicitada(UUID pedidoId, String clienteId){};

    public record EntregaCriada(UUID pedidoId,  String clienteId){};

    public record PedidoConfirmado(UUID pedidoId, String clienteId, BigDecimal total) {};

}
