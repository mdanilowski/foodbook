package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.model.Recipe;

public class RecipeDetailsPresenter extends BasePresenter {

    RecipeDetailsView view;
    RecipeDetailsModel model;

    Recipe recipe;

    public RecipeDetailsPresenter(RecipeDetailsView view, RecipeDetailsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        recipe = model.getRecipeFromIntent();
        setRecipeContent();
    }

    private void setRecipeContent() {
        if (recipe.getPhotosUrls() != null) {
            view.setPagerImages(recipe.getPhotosUrls());
        }
        view.setTvRecipeName(recipe.getName());
        view.setTvDescription(recipe.getDescription());
        view.setTvIngredients(recipe.getIngredients());
        view.setTvTags(recipe.getTags());
    }

    @Override
    public void onDestroy() {

    }
}
