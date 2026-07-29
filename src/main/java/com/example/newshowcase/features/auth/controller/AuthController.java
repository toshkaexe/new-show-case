package com.example.newshowcase.features.auth.controller;

import com.example.newshowcase.features.adapter.EmailService;
import com.example.newshowcase.features.auth.dto.EmailRequest;
import com.example.newshowcase.features.auth.dto.LoginRequest;
import com.example.newshowcase.features.auth.dto.RegistrationRequest;
import com.example.newshowcase.features.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest loginDto) {
        return authService.login(loginDto.getLoginOrEmail(), loginDto.getPassword());
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registration(@Valid @RequestBody RegistrationRequest registrationDto) {
        authService.registration(registrationDto.getLogin(), registrationDto.getEmail(), registrationDto.getPassword());
    }

    @GetMapping("/me")
    public Map<String, String> getMe(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return authService.getMe(userId);
    }

    @PostMapping("/registration-email-resending")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registrationEmailResending(@Valid @RequestBody EmailRequest emailDto) {
        emailService.send(emailDto.getEmail());
    }
}
