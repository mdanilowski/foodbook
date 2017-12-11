package pl.mdanilowski.foodbook.model.userUpdates;


import pl.mdanilowski.foodbook.model.Comment;

public class MyRecipeNewComment extends UserUpdatesBase {

    private Comment comment;
    private String oid;

    public MyRecipeNewComment() {
    }

    public MyRecipeNewComment(Comment comment, String oid) {
        this.comment = comment;
        this.oid = oid;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}