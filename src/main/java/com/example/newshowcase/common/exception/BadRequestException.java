package com.example.newshowcase.common.exception;

import java.util.List;
import java.util.Map;

public class BadRequestException extends RuntimeException {

    private final List<Map<String, String>> errors;

    public BadRequestException(List<Map<String, String>> errors) {
        super("Bad request");
        this.errors = errors;
    }

    public BadRequestException(String message, String field) {
        super(message);
        this.errors = List.of(Map.of("message", message, "field", field));
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}
