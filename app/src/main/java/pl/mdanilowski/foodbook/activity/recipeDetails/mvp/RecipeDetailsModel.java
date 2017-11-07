package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.model.Recipe;

public class RecipeDetailsModel {

    RecipeDetailsActivity activity;

    public RecipeDetailsModel(RecipeDetailsActivity activity) {
        this.activity = activity;
    }

    public Recipe getRecipeFromIntent() {
        return (Recipe) activity.getIntent().getSerializableExtra(RecipeDetailsActivity.RECIPE);
    }
}