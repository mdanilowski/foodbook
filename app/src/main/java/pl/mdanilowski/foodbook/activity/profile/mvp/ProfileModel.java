package pl.mdanilowski.foodbook.activity.profile.mvp;


import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;

public class ProfileModel {

    private ProfileActivity activity;

    public ProfileModel(ProfileActivity activity) {
        this.activity = activity;
    }

    public String getUserUid() {
        return activity.getIntent().getStringExtra(ProfileActivity.USER_UID);
    }
}
