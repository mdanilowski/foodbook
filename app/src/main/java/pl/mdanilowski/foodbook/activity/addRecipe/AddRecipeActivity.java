package pl.mdanilowski.foodbook.activity.addRecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.addRecipe.dagger.AddRecipeModule;
import pl.mdanilowski.foodbook.activity.addRecipe.dagger.DaggerAddRecipeComponent;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipePresenter;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipeView;
import pl.mdanilowski.foodbook.app.App;

public class AddRecipeActivity extends AppCompatActivity {

    @Inject
    AddRecipeView view;

    @Inject
    AddRecipePresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AddRecipeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerAddRecipeComponent.builder()
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .addRecipeModule(new AddRecipeModule(this))
                .build()
                .inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
