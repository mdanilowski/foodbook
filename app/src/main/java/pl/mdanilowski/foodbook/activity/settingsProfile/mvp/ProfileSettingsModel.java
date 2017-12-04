package pl.mdanilowski.foodbook.activity.settingsProfile.mvp;


import android.net.Uri;

import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.settingsProfile.ProfileSettingsActivity;
import pl.mdanilowski.foodbook.model.User;

public class ProfileSettingsModel {

    private ProfileSettingsActivity activity;

    public ProfileSettingsModel(ProfileSettingsActivity activity) {
        this.activity = activity;
    }

    public ProfileSettingsActivity getActivity() {
        return activity;
    }

    User getUserFromIntent() {
        return (User) activity.getIntent().getSerializableExtra(ProfileSettingsActivity.USER);
    }

    void finishActivity(Uri avatar, Uri back, User updatedUser) {
        DashboardActivity.start(activity, updatedUser, avatar, back);
    }
}