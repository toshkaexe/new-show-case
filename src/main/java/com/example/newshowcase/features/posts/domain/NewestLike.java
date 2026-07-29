package com.example.newshowcase.features.posts.domain;

public class NewestLike {

    private String addedAt;
    private String userId;
    private String login;

    public NewestLike() {}

    public NewestLike(String addedAt, String userId, String login) {
        this.addedAt = addedAt;
        this.userId = userId;
        this.login = login;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
