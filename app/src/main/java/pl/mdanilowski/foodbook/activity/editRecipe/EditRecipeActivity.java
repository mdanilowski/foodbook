package pl.mdanilowski.foodbook.activity.editRecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.editRecipe.dagger.DaggerEditRecipeComponent;
import pl.mdanilowski.foodbook.activity.editRecipe.dagger.EditRecipeModule;
import pl.mdanilowski.foodbook.activity.editRecipe.mvp.EditRecipePresenter;
import pl.mdanilowski.foodbook.activity.editRecipe.mvp.EditRecipeView;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;

public class EditRecipeActivity extends BaseActivity {

    public static final String RECIPE_FOR_EDITING = "RECIPE_FOR_EDITING";

    @Inject
    EditRecipeView view;

    @Inject
    EditRecipePresenter presenter;

    public static void start(Context context, Recipe recipe) {
        Intent intent = new Intent(context, EditRecipeActivity.class);
        intent.putExtra(RECIPE_FOR_EDITING, recipe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerEditRecipeComponent.builder()
                .editRecipeModule(new EditRecipeModule(this))
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}