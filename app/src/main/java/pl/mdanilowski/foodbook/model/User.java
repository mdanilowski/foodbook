package pl.mdanilowski.foodbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private int gender;
    private String avatarUrl;
    private String backgroundImage;
    private int totalLikes;
    private int recipesCount;
    private int followrsCount;
    private String aboutMe;
    private String country;
    private String city;
    private List<Follower> followers = new ArrayList<>();
    private List<Follower> following = new ArrayList<>();

    public User() {
    }

    public User(String uid, String name, String email, int gender, String avatarUrl, String backgroundImage, int totalLikes, int recipesCount, int followrsCount, String aboutMe, String country, String city, List<Follower> followers) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.backgroundImage = backgroundImage;
        this.totalLikes = totalLikes;
        this.recipesCount = recipesCount;
        this.followrsCount = followrsCount;
        this.aboutMe = aboutMe;
        this.country = country;
        this.city = city;
        this.followers = followers;
    }

    public List<Follower> getFollowing() {
        return following;
    }

    public void setFollowing(List<Follower> following) {
        this.following = following;
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

    public int getFollowrsCount() {
        return followrsCount;
    }

    public void setFollowrsCount(int followrsCount) {
        this.followrsCount = followrsCount;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
