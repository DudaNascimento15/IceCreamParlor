package com.IceCreamParlor.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

public class CaixaEvents {
    public record PagamentoAprovado(UUID pedidoId, BigDecimal valor, String clientId) {}
    public record PagamentoNegado(UUID pedidoId, String motivo, String clientId) {}
}
