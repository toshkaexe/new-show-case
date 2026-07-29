package com.example.newshowcase.dto;

import jakarta.validation.constraints.NotBlank;

public class ConfirmationCodeRequest {

    @NotBlank(message = "code is required")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
