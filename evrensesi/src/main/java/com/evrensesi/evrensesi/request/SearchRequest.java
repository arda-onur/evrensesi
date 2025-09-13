package com.evrensesi.evrensesi.request;

import jakarta.validation.constraints.NotBlank;

public record SearchRequest(
        @NotBlank(message = "search.comment.request.not.blank.message")
        String search
){
}