package com.stylepin.exception;
import com.stylepin.dto.ApiErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ApiErrorDTO> error(int status, String message, Map<String,String> fields) {
        return ResponseEntity.status(status).body(new ApiErrorDTO(status, "HTTP_" + status, message, fields));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorDTO> invalid(MethodArgumentNotValidException e) {
        Map<String,String> fields = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f -> fields.putIfAbsent(f.getField(), f.getDefaultMessage()));
        return error(400, "Please check the highlighted fields", fields);
    }
    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiErrorDTO> api(ApiException e) { return error(e.getStatus(), e.getMessage(), Map.of()); }
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiErrorDTO> missing() { return error(404, "Resource not found", Map.of()); }
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ApiErrorDTO> auth() { return error(401, "Sign in required or credentials invalid", Map.of()); }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiErrorDTO> denied() { return error(403, "You do not have access to this resource", Map.of()); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiErrorDTO> duplicate() { return error(409, "A record with these details already exists", Map.of()); }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ApiErrorDTO> malformed() { return error(400, "Invalid request", Map.of()); }
    @ExceptionHandler({jakarta.validation.ConstraintViolationException.class, org.springframework.web.method.annotation.HandlerMethodValidationException.class})
    ResponseEntity<ApiErrorDTO> constraint() { return error(400, "Invalid parameter or field value", Map.of()); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorDTO> unexpected() { return error(500, "Unable to complete the request", Map.of()); }
}
