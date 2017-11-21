package pl.mdanilowski.foodbook.model.userUpdates;


import java.io.Serializable;

import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;

public class NewFollowersComment extends UserUpdatesBase implements Serializable {

    private Recipe recipe;
    private User owner;
    private Comment comment;

    public NewFollowersComment() {
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
