package pl.mdanilowski.foodbook.activity.recipeDetails.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.activity.recipeDetails.mvp.RecipeDetailsModel;
import pl.mdanilowski.foodbook.activity.recipeDetails.mvp.RecipeDetailsPresenter;
import pl.mdanilowski.foodbook.activity.recipeDetails.mvp.RecipeDetailsView;

@Module
public class RecipeDetailsModule {

    private RecipeDetailsActivity activity;

    public RecipeDetailsModule(RecipeDetailsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @RecipeDetilsScope
    public RecipeDetailsActivity activity() {
        return activity;
    }

    @Provides
    @RecipeDetilsScope
    public RecipeDetailsModel model() {
        return new RecipeDetailsModel(activity);
    }

    @Provides
    @RecipeDetilsScope
    public RecipeDetailsView view() {
        return new RecipeDetailsView(activity);
    }

    @Provides
    @RecipeDetilsScope
    public RecipeDetailsPresenter presenter(RecipeDetailsView view, RecipeDetailsModel model) {
        return new RecipeDetailsPresenter(view, model);
    }
}
