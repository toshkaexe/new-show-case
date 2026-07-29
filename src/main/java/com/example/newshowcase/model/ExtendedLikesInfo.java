package com.example.newshowcase.model;

import java.util.List;

public class ExtendedLikesInfo {

    private int likesCount;
    private int dislikesCount;
    private String myStatus;
    private List<NewestLike> newestLikes;

    public ExtendedLikesInfo() {}

    public ExtendedLikesInfo(int likesCount, int dislikesCount, String myStatus, List<NewestLike> newestLikes) {
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.myStatus = myStatus;
        this.newestLikes = newestLikes;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public String getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(String myStatus) {
        this.myStatus = myStatus;
    }

    public List<NewestLike> getNewestLikes() {
        return newestLikes;
    }

    public void setNewestLikes(List<NewestLike> newestLikes) {
        this.newestLikes = newestLikes;
    }
}
