package pl.mdanilowski.foodbook.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String name;
    private String surename;
    private String email;
    private int gender;
    private String avatarUrl;
    private int totalLikes;
    private int recipesCount;
    private List<Friend> friends;

    public User() {
    }

    public User(String name, String surename, String email, int gender, String avatarUrl, int totalLikes, int recipesCount, List<Friend> friends) {
        this.name = name;
        this.surename = surename;
        this.email = email;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.totalLikes = totalLikes;
        this.recipesCount = recipesCount;
        this.friends = friends;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getRecipesCount() {
        return recipesCount;
    }

    public void setRecipesCount(int recipesCount) {
        this.recipesCount = recipesCount;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}
