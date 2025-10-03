package com.IceCreamParlor.dto.events;


import java.util.UUID;

public class EstoqueEvents {

    public record ReservaSolicitada(UUID pedidoId, String clienteId) {}

    public record ReservaConfirmada(UUID pedidoId) {}

    public record ReservaNegada(UUID pedidoId, String motivo) {}

}

