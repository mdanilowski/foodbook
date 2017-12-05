package pl.mdanilowski.foodbook.model.userUpdates;


import java.io.Serializable;

import pl.mdanilowski.foodbook.model.User;

public class MyRecipeLike extends UserUpdatesBase implements Serializable {

    private User liker;

    public MyRecipeLike() {
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

}
