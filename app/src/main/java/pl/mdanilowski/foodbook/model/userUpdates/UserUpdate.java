package pl.mdanilowski.foodbook.model.userUpdates;


import java.io.Serializable;

public class UserUpdate implements Serializable {

    private String rid;
    private String uid;
    private String cid;
    private String oid;
    private String recipeName;
    private String recipeImage;
    private String recipeOwnerName;
    private String userAvatar;
    private String userName;
    private long addDate;
    private UpdateType updateType;

    public UserUpdate() {
    }

    public UserUpdate(String rid, String uid, String cid, String oid, String recipeName, String recipeImage, String recipeOwnerName, String userAvatar, String userName, long addDate) {
        this.rid = rid;
        this.uid = uid;
        this.cid = cid;
        this.oid = oid;
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
        this.recipeOwnerName = recipeOwnerName;
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.addDate = addDate;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getRecipeOwnerName() {
        return recipeOwnerName;
    }

    public void setRecipeOwnerName(String recipeOwnerName) {
        this.recipeOwnerName = recipeOwnerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
}
