package com.example.newshowcase.features.blogs.dto;

import com.example.newshowcase.features.blogs.domain.Blog;

public class BlogOutputModel {

    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private boolean isMembership;
    private String createdAt;

    public static BlogOutputModel from(Blog blog) {
        BlogOutputModel model = new BlogOutputModel();
        model.id = blog.getId();
        model.name = blog.getName();
        model.description = blog.getDescription();
        model.websiteUrl = blog.getWebsiteUrl();
        model.isMembership = blog.isMembership();
        model.createdAt = blog.getCreatedAt();
        return model;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public boolean isMembership() {
        return isMembership;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
