package pl.mdanilowski.foodbook.activity.likedRecipes.mvp;


import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;

public class LikedRecipesModel {
    private LikedRecipesActivity activity;

    public LikedRecipesModel(LikedRecipesActivity activity) {
        this.activity = activity;
    }

    public LikedRecipesActivity getActivity() {
        return activity;
    }

    public void startRecipeDetailsActivity(String uid, String rid){
        RecipeDetailsActivity.start(activity, uid, rid);
    }
}
