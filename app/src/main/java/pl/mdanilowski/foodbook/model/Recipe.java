package pl.mdanilowski.foodbook.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable {

    @SerializedName("rid")
    private String rid;
    @SerializedName("oid")
    private String oid;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("likes")
    private int likes;
    @SerializedName("commentCount")
    private int commentCount;
    @SerializedName("ownerName")
    private String ownerName;
    @SerializedName("isMine")
    private boolean isMine;
    @SerializedName("addDate")
    private Date addDate;
    @SerializedName("ingredients")
    private List<String> ingredients;
    @SerializedName("comments")
    private List<Comment> comments = new ArrayList<>();
    @SerializedName("tags")
    private List<String> tags = new ArrayList<>();
    @SerializedName("photos")
    private List<String> photosUrls = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(String oid,
                  String name,
                  String description,
                  int likes,
                  String ownerName,
                  boolean isMine,
                  Date addDate,
                  List<String> ingredients,
                  List<String> tags,
                  List<String> photosUrls) {
        this.oid = oid;
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.ownerName = ownerName;
        this.isMine = isMine;
        this.addDate = addDate;
        this.ingredients = ingredients;
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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
