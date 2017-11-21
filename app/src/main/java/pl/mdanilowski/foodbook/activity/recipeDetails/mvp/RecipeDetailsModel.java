package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.model.User;

public class RecipeDetailsModel {

    RecipeDetailsActivity activity;

    public RecipeDetailsModel(RecipeDetailsActivity activity) {
        this.activity = activity;
    }

    public String getRidFromIntent() {
        return activity.getIntent().getStringExtra(RecipeDetailsActivity.RECIPE_ID);
    }

    public String getUidFromIntent(){
        return activity.getIntent().getStringExtra(RecipeDetailsActivity.USER_ID);
    }

    public void startProfileActivity(String uid){
        ProfileActivity.start(activity, uid);
    }

    public void startProfileActivity(User user){
        ProfileActivity.start(activity, user);
    }
}