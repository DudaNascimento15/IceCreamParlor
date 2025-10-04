package com.IceCreamParlor.messaging;

import java.time.Instant;

public record Envelope<T>(
        String messageId,
        String correlationId,
        String usuario,
        Instant createdAt,
        T data
) {}