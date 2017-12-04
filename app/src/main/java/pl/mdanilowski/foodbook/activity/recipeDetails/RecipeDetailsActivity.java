package pl.mdanilowski.foodbook.activity.recipeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.recipeDetails.dagger.DaggerRecipeDetailsComponent;
import pl.mdanilowski.foodbook.activity.recipeDetails.dagger.RecipeDetailsModule;
import pl.mdanilowski.foodbook.activity.recipeDetails.mvp.RecipeDetailsPresenter;
import pl.mdanilowski.foodbook.activity.recipeDetails.mvp.RecipeDetailsView;
import pl.mdanilowski.foodbook.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String RECIPE = "RECIPE";

    @Inject
    RecipeDetailsView view;

    @Inject
    RecipeDetailsPresenter presenter;

    public static void start(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(RECIPE, recipe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerRecipeDetailsComponent.builder().recipeDetailsModule(new RecipeDetailsModule(this)).build().inject(this);

        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
