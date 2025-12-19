package com.josemiguel.ordermanagement.infraestructura.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handler global de excepciones para la aplicación
 * - Captura errores de validación y otros tipos de excepciones
 * - Devuelve responses con el código HTTP y el mensaje correspondiente
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Maneja errores de validación de los DTOs anotados con @Valid
     * - Recolecta todos los errores de campo y los devuelve como JSON
     * - HTTP status: 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    Map<String, String> e = new HashMap<>();
                    e.put("field", error.getField());
                    e.put("message", error.getDefaultMessage());
                    return e;
                })
                .collect(Collectors.toList());

        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation failed");
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de tipo PedidoNoEncontradoException
     * - Retorna mensaje con HTTP status 404 Not Found
     */
    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handlePedidoNoEncontrado(PedidoNoEncontradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo EstadoFinalException
     * - Retorna mensaje con HTTP status 400 Bad Request
     */
    @ExceptionHandler(EstadoFinalException.class)
    public ResponseEntity<Map<String, Object>> handleEstadoFinal(EstadoFinalException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja cualquier otra excepción no controlada
     * - Retorna mensaje genérico con HTTP status 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", "Ocurrió un error en el servidor: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
