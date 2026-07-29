package com.example.newshowcase.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        List<Map<String, String>> errorsMessages = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", error.getDefaultMessage());
            errorMap.put("field", error.getField());
            errorsMessages.add(errorMap);
        }

        Map<String, List<Map<String, String>>> response = new HashMap<>();
        response.put("errorsMessages", errorsMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleBadRequest(BadRequestException ex) {
        Map<String, List<Map<String, String>>> response = new HashMap<>();
        response.put("errorsMessages", ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
