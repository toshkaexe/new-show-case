package com.example.newshowcase.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blogs")
public class Blog {

    @Id
    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private String createdAt;
    private boolean isMembership;

    public Blog() {}

    public Blog(String name, String description, String websiteUrl) {
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.createdAt = java.time.Instant.now().toString();
        this.isMembership = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isMembership() {
        return isMembership;
    }

    public void setMembership(boolean membership) {
        isMembership = membership;
    }
}
