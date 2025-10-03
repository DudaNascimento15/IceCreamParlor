package com.IceCreamParlor.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

public class CaixaEvents {

    //publish
    public record PagamentoIniciado(UUID pedidoId, String clienteId, BigDecimal valorTotal){};

    //receive
    public record PagamentoAprovado(UUID pedidoId, BigDecimal valor) {}
    public record PagamentoNegado(UUID pedidoId, String motivo) {}


}
