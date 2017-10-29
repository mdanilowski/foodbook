package pl.mdanilowski.foodbook.model;

import java.io.Serializable;

public class Friend implements Serializable {

    private String uid;
    private String avatarUrl;
    private String name;
    private String surename;
    private int recipeCount;
    private int totalLikes;

    public Friend() {
    }

    public Friend(String uid, String avatarUrl, String name, String surename, int recipeCount, int totalLikes) {
        this.uid = uid;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.surename = surename;
        this.recipeCount = recipeCount;
        this.totalLikes = totalLikes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }
}
