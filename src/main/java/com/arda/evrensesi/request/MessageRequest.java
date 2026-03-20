package com.arda.evrensesi.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record MessageRequest(
        @Min(value = 0, message = "{star.coordinate.x.min}")
        @Max(value = 10000, message = "{star.coordinate.x.max}")
        int x,
        @Min(value = 0, message = "{star.coordinate.y.min}")
        @Max(value = 10000, message = "{star.coordinate.y.max}")
        int y
) {
}
