package com.IceCreamParlor.dto.events;

import java.util.UUID;

public record NotificacaoCliente(
    UUID pedidoId,
    String clienteId,
    String tipoEvento,
    String mensagem
) {}
