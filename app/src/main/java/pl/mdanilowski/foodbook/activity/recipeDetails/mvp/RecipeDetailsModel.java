package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;

public class RecipeDetailsModel {

    RecipeDetailsActivity activity;

    public RecipeDetailsModel(RecipeDetailsActivity activity) {
        this.activity = activity;
    }

    public Recipe getRecipeFromIntent() {
        return (Recipe) activity.getIntent().getSerializableExtra(RecipeDetailsActivity.RECIPE);
    }

    public void startProfileActivity(String uid){
        ProfileActivity.start(activity, uid);
    }

    public void startProfileActivity(User user){
        ProfileActivity.start(activity, user);
    }
}