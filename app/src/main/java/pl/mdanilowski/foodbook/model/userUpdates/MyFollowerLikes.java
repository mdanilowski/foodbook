package pl.mdanilowski.foodbook.model.userUpdates;

import java.io.Serializable;

import pl.mdanilowski.foodbook.model.User;


public class MyFollowerLikes extends UserUpdatesBase implements Serializable {

    private User liker;

    public MyFollowerLikes() {
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }
}
