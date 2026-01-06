package com.example.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String correlationId,
        int status,
        LocalDateTime timestamp) {
}
