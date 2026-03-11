package com.arda.evrensesi.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StarRequest(
        @NotBlank(message = "{star.message.cannot.be.blank}")
        @Size(max = 600, message = "{star.message.max.characters}")
        String message,

        @Min(value = 0, message = "{star.coordinate.x.min}")
        @Max(value = 10000, message = "{star.coordinate.x.max}")
        int x,

        @Min(value = 0, message = "{star.coordinate.y.min}")
        @Max(value = 10000, message = "{star.coordinate.y.max}")
        int y
) {
    public StarRequest {
        int wordCount = message.trim().split("\\s+").length;

        if (wordCount > 50) {
            throw new IllegalArgumentException("star.message.max.words");
        }
    }
}
