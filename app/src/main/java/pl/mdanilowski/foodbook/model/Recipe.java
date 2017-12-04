package pl.mdanilowski.foodbook.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable {

    private String rid;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("likes")
    private int likes;
    @SerializedName("isMine")
    private boolean isMine;
    @SerializedName("addDate")
    private Date addDate;
    @SerializedName("ingredients")
    private List<String> ingredients;
    @SerializedName("comments")
    private List<Comment> comments;
    @SerializedName("tags")
    private List<String> tags;
    @SerializedName("photos")
    private List<String> photosUrls;

    public Recipe() {
    }

    public Recipe(String name, String description, int likes, boolean isMine, Date addDate, List<String> ingredients, List<Comment> comments, List<String> tags, List<String> photosUrls) {
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.isMine = isMine;
        this.addDate = addDate;
        this.ingredients = ingredients;
        this.comments = comments;
        this.tags = tags;
        this.photosUrls = photosUrls;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getPhotosUrls() {
        return photosUrls;
    }

    public void setPhotosUrls(List<String> photosUrls) {
        this.photosUrls = photosUrls;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
