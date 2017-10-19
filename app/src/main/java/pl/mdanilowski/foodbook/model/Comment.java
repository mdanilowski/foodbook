package pl.mdanilowski.foodbook.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {

    @SerializedName("uid")
    private String uid;
    @SerializedName("name")
    private String name;
    @SerializedName("surename")
    private String surename;
    @SerializedName("avatar")
    private String avatarUrl;
    @SerializedName("comment")
    private String commentText;

    public Comment() {
    }

    public Comment(String uid, String name, String surename, String avatarUrl, String commentText) {
        this.uid = uid;
        this.name = name;
        this.surename = surename;
        this.avatarUrl = avatarUrl;
        this.commentText = commentText;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
