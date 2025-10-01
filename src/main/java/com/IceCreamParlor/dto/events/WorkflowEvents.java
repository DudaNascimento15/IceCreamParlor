package com.IceCreamParlor.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

public class WorkflowEvents {

    //publish
    public record PagamentoIniciado(UUID pedidoId, String clienteId, BigDecimal valorTotal){};
    public record ReservaSolicitada(UUID pedidoId){};
    public record EntregaCriada(UUID pedidoId,  String clienteId){};

    //receive
    public record PagamentoAprovado(UUID pedidoId, BigDecimal valor) {}
    public record PagamentoNegado(UUID pedidoId, String motivo) {}
    public record ReservaConfirmada(UUID pedidoId) {}
    public record ReservaNegada(UUID pedidoId, String motivo) {}

    //publish
    public record PedidoConfirmado(UUID pedidoId, String clienteId, BigDecimal total) {}

}
