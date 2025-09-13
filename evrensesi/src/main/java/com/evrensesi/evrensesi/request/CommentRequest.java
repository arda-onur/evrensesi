package com.evrensesi.evrensesi.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "create.comment.request.not.blank.message")
        String comment,
        @NotBlank
        double x,
        @NotBlank
        double y
){
}
