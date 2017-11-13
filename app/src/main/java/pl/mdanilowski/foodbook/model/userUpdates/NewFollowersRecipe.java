package pl.mdanilowski.foodbook.model.userUpdates;


import java.io.Serializable;

import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;

public class NewFollowersRecipe extends UserUpdatesBase implements Serializable {

    private User user;
    private Recipe recipe;

    public NewFollowersRecipe() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
