package com.example.FacturadorFXSmart.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, Object> body = new HashMap<>();
        body.put("error", "validation_error");
        body.put("details", ex.getBindingResult().getFieldErrors().stream().map(f -> Map.of("field", f.getField(), "message", f.getDefaultMessage())).toList());
        return ResponseEntity.badRequest().body(body);
    }
}
