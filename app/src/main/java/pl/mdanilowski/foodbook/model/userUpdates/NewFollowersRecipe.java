package pl.mdanilowski.foodbook.model.userUpdates;


import java.io.Serializable;

import pl.mdanilowski.foodbook.model.User;

public class NewFollowersRecipe extends UserUpdatesBase implements Serializable {

    private User user;

    public NewFollowersRecipe() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
