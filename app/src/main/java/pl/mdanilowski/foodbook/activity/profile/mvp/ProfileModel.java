package pl.mdanilowski.foodbook.activity.profile.mvp;


import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.usersRecipes.UsersRecipesActivity;
import pl.mdanilowski.foodbook.model.User;

public class ProfileModel {

    private ProfileActivity activity;

    public ProfileModel(ProfileActivity activity) {
        this.activity = activity;
    }

    public String getUserUid() {
        return activity.getIntent().getStringExtra(ProfileActivity.USER_UID);
    }

    public User getUserFromIntent() {
        return (User) activity.getIntent().getSerializableExtra(ProfileActivity.USER);
    }

    public void startUsersRecipesActivity(User user) {
        UsersRecipesActivity.start(activity, user);
    }
}
