package com.example.newshowcase.features.posts.dto;

import com.example.newshowcase.features.posts.domain.ExtendedLikesInfo;
import com.example.newshowcase.features.posts.domain.NewestLike;
import com.example.newshowcase.features.posts.domain.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostOutputModel {

    private String id;
    private String title;
    private String shortDescription;
    private String content;
    private String blogId;
    private String blogName;
    private String createdAt;
    private ExtendedLikesInfoOutput extendedLikesInfo;

    public static PostOutputModel from(Post post) {
        PostOutputModel model = new PostOutputModel();
        model.id = post.getId();
        model.title = post.getTitle();
        model.shortDescription = post.getShortDescription();
        model.content = post.getContent();
        model.blogId = post.getBlogId();
        model.blogName = post.getBlogName();
        model.createdAt = post.getCreatedAt();

        if (post.getExtendedLikesInfo() != null) {
            ExtendedLikesInfo info = post.getExtendedLikesInfo();
            List<NewestLikeOutput> likes = info.getNewestLikes() != null
                    ? info.getNewestLikes().stream()
                    .map(l -> new NewestLikeOutput(l.getAddedAt(), l.getUserId(), l.getLogin()))
                    .collect(Collectors.toList())
                    : List.of();
            model.extendedLikesInfo = new ExtendedLikesInfoOutput(
                    info.getLikesCount(), info.getDislikesCount(), info.getMyStatus(), likes
            );
        }

        return model;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getContent() {
        return content;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getBlogName() {
        return blogName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ExtendedLikesInfoOutput getExtendedLikesInfo() {
        return extendedLikesInfo;
    }

    public record ExtendedLikesInfoOutput(int likesCount, int dislikesCount, String myStatus,
                                          List<NewestLikeOutput> newestLikes) {}

    public record NewestLikeOutput(String addedAt, String userId, String login) {}
}
