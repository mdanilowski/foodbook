package pl.mdanilowski.foodbook.model.userUpdates;

import java.io.Serializable;

import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;


public class MyFollowerLikes extends UserUpdatesBase implements Serializable {

    private User liker;
    private Recipe recipe;

    public MyFollowerLikes() {
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
