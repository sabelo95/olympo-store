package com.producto_service.Exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        ErrorResponse response = ErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", exception.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .message("Invalid argument provided")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errorDetails)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrity(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("No se puede eliminar el producto porque tiene registros asociados.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception e) {
        String cause = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        if (cause != null && (cause.contains("violates foreign key") || cause.contains("constraint"))) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el producto porque tiene registros asociados.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    @Builder
    @Getter
    public static class ErrorResponse {
        private String message;
        private int status;
        private LocalDateTime timestamp;
        private Map<String, String> errors;
    }
}

