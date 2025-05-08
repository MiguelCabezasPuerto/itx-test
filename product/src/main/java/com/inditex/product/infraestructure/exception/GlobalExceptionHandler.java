package com.inditex.product.infraestructure.exception;

import com.inditex.product.domain.exception.PriceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PriceException.class)
    public ResponseEntity<Map<String, Object>> handlePriceException(PriceException e) {
        log.error("Error capturado: {} - {}", e.getErrorType(), e.getMessage());

        Map<String, Object> errorResponse = Map.of(
                "errorType", e.getErrorType().name(),
                "message", e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
        log.error("Error inesperado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno del servidor."));
    }
}

