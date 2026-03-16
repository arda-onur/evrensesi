package com.arda.evrensesi.event;

import java.util.UUID;

public record StarCreatedEvent(
        UUID id,
        String message,
        int x,
        int y) {
}
