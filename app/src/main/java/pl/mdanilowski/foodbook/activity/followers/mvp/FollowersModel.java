package pl.mdanilowski.foodbook.activity.followers.mvp;


import pl.mdanilowski.foodbook.activity.followers.FollowersActivity;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;

public class FollowersModel {

    private FollowersActivity activity;

    public FollowersModel(FollowersActivity activity) {
        this.activity = activity;
    }

    public void startProfileActivity(String uid){
        ProfileActivity.start(activity, uid);
    }
}
