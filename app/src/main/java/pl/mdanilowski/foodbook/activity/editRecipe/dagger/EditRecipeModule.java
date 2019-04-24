package pl.mdanilowski.foodbook.activity.editRecipe.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.editRecipe.EditRecipeActivity;
import pl.mdanilowski.foodbook.activity.editRecipe.mvp.EditRecipeModel;
import pl.mdanilowski.foodbook.activity.editRecipe.mvp.EditRecipePresenter;
import pl.mdanilowski.foodbook.activity.editRecipe.mvp.EditRecipeView;

@Module
public class EditRecipeModule {

    private EditRecipeActivity activity;

    public EditRecipeModule(EditRecipeActivity activity) {
        this.activity = activity;
    }

    @Provides
    @EditRecipeScope
    public EditRecipeActivity activity() {
        return activity;
    }

    @Provides
    @EditRecipeScope
    public EditRecipeModel model() {
        return new EditRecipeModel(activity);
    }

    @Provides
    @EditRecipeScope
    public EditRecipeView view() {
        return new EditRecipeView(activity);
    }

    @Provides
    @EditRecipeScope
    public EditRecipePresenter presenter(EditRecipeView view, EditRecipeModel model) {
        return new EditRecipePresenter(view, model);
    }
}