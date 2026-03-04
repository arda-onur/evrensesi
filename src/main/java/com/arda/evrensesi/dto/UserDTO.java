package com.arda.evrensesi.dto;

import java.time.Instant;

public record UserDTO(
        String email,
        Instant createdAt
)
{
}
