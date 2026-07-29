package com.example.newshowcase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String login;
    private String password;
    private String email;
    private Instant createdAt;
    private boolean isConfirmed;
    private String confirmationCode;
    private Instant confirmationCodeExpiration;
    private String recoveryCode;
    private Instant recoveryCodeExpiration;

    public User() {}

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.createdAt = Instant.now();
        this.isConfirmed = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Instant getConfirmationCodeExpiration() {
        return confirmationCodeExpiration;
    }

    public void setConfirmationCodeExpiration(Instant confirmationCodeExpiration) {
        this.confirmationCodeExpiration = confirmationCodeExpiration;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public Instant getRecoveryCodeExpiration() {
        return recoveryCodeExpiration;
    }

    public void setRecoveryCodeExpiration(Instant recoveryCodeExpiration) {
        this.recoveryCodeExpiration = recoveryCodeExpiration;
    }
}
