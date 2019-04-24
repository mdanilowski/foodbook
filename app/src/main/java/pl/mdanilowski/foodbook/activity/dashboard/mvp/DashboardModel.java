package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import java.util.List;

import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.findFriends.FindFriendsActivity;
import pl.mdanilowski.foodbook.activity.followers.FollowersActivity;
import pl.mdanilowski.foodbook.activity.following.FollowingActivity;
import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.activity.settingsProfile.ProfileSettingsActivity;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.RecipeQuery;
import pl.mdanilowski.foodbook.model.User;

public class DashboardModel {

    DashboardActivity activity;

    public DashboardModel(DashboardActivity activity) {
        this.activity = activity;
    }

    public DashboardActivity getActivity() {
        return activity;
    }

    FragmentManager getFragmentManager() {
        return activity.getSupportFragmentManager();
    }

    public void startAddRecipeActivity() {
        AddRecipeActivity.start(activity);
    }

    public Intent getIntent() {
        return activity.getIntent();
    }

    boolean getIsRecipeAddedExtra() {
        return activity.getIntent().getBooleanExtra(DashboardActivity.IS_RECIPE_ADDED, false);
    }

    List<Uri> getRecipeUriListFromIntent() {
        return activity.getIntent().getParcelableArrayListExtra(DashboardActivity.IMAGES);
    }

    boolean getIsNewIntentSearch() {
        return Intent.ACTION_SEARCH.equals(activity.getIntent().getAction());
    }

    Recipe getRecipeFromIntent() {
        return (Recipe) activity.getIntent().getSerializableExtra(DashboardActivity.RECIPE);
    }

    RecipeQuery getRecipeQueryFromIntent() {
        return (RecipeQuery) activity.getIntent().getSerializableExtra(DashboardActivity.RECIPE_QUERY);
    }

    boolean isUserUpdatedIntent() {
        return activity.getIntent().getBooleanExtra(DashboardActivity.IS_USER_UPDATED, false);
    }

    boolean isDeepLinkIntent() {
        return activity.getIntent().getBooleanExtra(DashboardActivity.DEEP_LINK_INTENT, false);
    }

    boolean isAfterRegister() {
        return activity.getIntent().getBooleanExtra(DashboardActivity.IS_AFTER_REGISTRATION, false);
    }

    User getRegisteredUser() {
        return (User) activity.getIntent().getSerializableExtra(DashboardActivity.REGISTERED_USER);
    }

    String getUidFromIntent (){
        return activity.getIntent().getStringExtra(DashboardActivity.UID_TAG);
    }

    String getRidFromIntent (){
        return activity.getIntent().getStringExtra(DashboardActivity.RID_TAG);
    }

    Uri getAvatarUriFromIntent() {
        return activity.getIntent().getParcelableExtra(DashboardActivity.AVATAR_URI);
    }

    Uri getBackgroundUriFromIntent() {
        return activity.getIntent().getParcelableExtra(DashboardActivity.BACKGROUND_URI);
    }

    User getUpdatedUserFromIntent() {
        return (User) activity.getIntent().getSerializableExtra(DashboardActivity.USER_UPDATED);
    }

    public void startProfileActivity(String uid) {
        ProfileActivity.start(activity, uid);
    }

    public void startRecipeDetailsActivity(String uid, String rid) {
        RecipeDetailsActivity.start(activity, uid, rid);
    }

    void startUserSettingsActivity(User foodbookUser) {
        ProfileSettingsActivity.start(activity, foodbookUser);
    }

    void startFollowingActivity() {
        FollowingActivity.start(activity);
    }

    void startFindFriendsActibity() {
        FindFriendsActivity.start(activity);
    }

    void startFollowersActivity(String uid) {
        FollowersActivity.start(activity, uid);
    }

    void startLikedRecipesActivity(){
        LikedRecipesActivity.start(activity);
    }

    public void startWelcomeActivity() {
        WelcomeActivity.start(activity);
    }
}

