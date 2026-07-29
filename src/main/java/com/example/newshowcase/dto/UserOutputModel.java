package com.example.newshowcase.dto;

import com.example.newshowcase.model.User;

public class UserOutputModel {

    private String id;
    private String login;
    private String email;
    private String createdAt;

    public static UserOutputModel from(User user) {
        UserOutputModel model = new UserOutputModel();
        model.id = user.getId();
        model.login = user.getLogin();
        model.email = user.getEmail();
        model.createdAt = user.getCreatedAt().toString();
        return model;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
