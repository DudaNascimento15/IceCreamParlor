package com.IceCreamParlor.dto.events;


import java.util.UUID;

public class EstoqueEvents {

     public record ReservaConfirmada(UUID pedidoId, String clienteId) {}

    public record ReservaNegada(UUID pedidoId, String motivo, String clienteId) {}

}

