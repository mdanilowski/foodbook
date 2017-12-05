package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import android.content.Intent;

import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.model.User;

public class RecipeDetailsModel {

    RecipeDetailsActivity activity;

    public RecipeDetailsModel(RecipeDetailsActivity activity) {
        this.activity = activity;
    }

    String getRidFromIntent() {
        return activity.getIntent().getStringExtra(RecipeDetailsActivity.RECIPE_ID);
    }

    String getOidFromIntent() {
        return activity.getIntent().getStringExtra(RecipeDetailsActivity.USER_ID);
    }

    void startProfileActivity(String uid) {
        ProfileActivity.start(activity, uid);
    }

    void startProfileActivity(User user) {
        ProfileActivity.start(activity, user.getUid());
    }

    void startShareIntent(Intent intent) {
        activity.startActivity(intent);
    }
}