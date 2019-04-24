package pl.mdanilowski.foodbook.activity.editRecipe.mvp;

import pl.mdanilowski.foodbook.activity.editRecipe.EditRecipeActivity;
import pl.mdanilowski.foodbook.model.Recipe;

public class EditRecipeModel {
    private EditRecipeActivity activity;

    public EditRecipeModel(EditRecipeActivity activity) {
        this.activity = activity;
    }

    Recipe getRecipeFromIntent(){
        return (Recipe) activity.getIntent().getSerializableExtra(EditRecipeActivity.RECIPE_FOR_EDITING);
    }

    void back() {
        activity.onBackPressed();
    }
}
