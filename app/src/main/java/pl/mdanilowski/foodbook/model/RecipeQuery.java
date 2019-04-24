package pl.mdanilowski.foodbook.model;

import java.io.Serializable;
import java.util.Map;


public class RecipeQuery implements Serializable {

    private Map<String, Boolean> queryStrings;
    private String rid;
    private String name;
    private String imageUrl;
    private String oid;
    private String ownerAvatarUrl;
    private String ownerName;

    public RecipeQuery(Map<String, Boolean> queryStrings, String rid, String name, String imageUrl, String oid, String ownerAvatarUrl, String ownerName) {
        this.queryStrings = queryStrings;
        this.rid = rid;
        this.name = name;
        this.imageUrl = imageUrl;
        this.oid = oid;
        this.ownerAvatarUrl = ownerAvatarUrl;
        this.ownerName = ownerName;
    }

    public Map<String, Boolean> getQueryStrings() {
        return queryStrings;
    }

    public void setQueryStrings(Map<String, Boolean> queryStrings) {
        this.queryStrings = queryStrings;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOwnerAvatarUrl() {
        return ownerAvatarUrl;
    }

    public void setOwnerAvatarUrl(String ownerAvatarUrl) {
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
