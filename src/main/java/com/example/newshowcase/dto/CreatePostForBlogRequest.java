package com.example.newshowcase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePostForBlogRequest {

    @NotBlank
    @Size(max = 30)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String shortDescription;

    @NotBlank
    @Size(max = 1000)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
