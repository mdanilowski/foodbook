package pl.mdanilowski.foodbook.activity.usersRecipes.mvp;


import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.activity.usersRecipes.UsersRecipesActivity;
import pl.mdanilowski.foodbook.model.User;

public class UsersRecipesModel {


    UsersRecipesActivity activity;

    public UsersRecipesModel(UsersRecipesActivity activity) {
        this.activity = activity;
    }

    public User getUserFromIntent() {
        return (User) activity.getIntent().getSerializableExtra(UsersRecipesActivity.USER);
    }

    public void startRecipeDetailsActivity(String uid, String rid){
        RecipeDetailsActivity.start(activity, uid, rid);
    }
}
