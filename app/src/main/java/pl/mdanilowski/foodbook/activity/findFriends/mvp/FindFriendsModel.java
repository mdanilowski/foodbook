package pl.mdanilowski.foodbook.activity.findFriends.mvp;


import android.content.Intent;

import pl.mdanilowski.foodbook.activity.findFriends.FindFriendsActivity;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;

public class FindFriendsModel {
    private FindFriendsActivity activity;

    public FindFriendsModel(FindFriendsActivity activity) {
        this.activity = activity;
    }

    public FindFriendsActivity getActivity() {
        return activity;
    }

    boolean getIsNewIntentSearch() {
        return Intent.ACTION_SEARCH.equals(activity.getIntent().getAction());
    }

    void startProfileActivity(String uid) {
        ProfileActivity.start(activity, uid);
    }
}
