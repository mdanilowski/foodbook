package pl.mdanilowski.foodbook.activity.usersRecipes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.usersRecipes.dagger.DaggerUsersRecipesComponent;
import pl.mdanilowski.foodbook.activity.usersRecipes.dagger.UsersRecipesModule;
import pl.mdanilowski.foodbook.activity.usersRecipes.mvp.UsersRecipesPresenter;
import pl.mdanilowski.foodbook.activity.usersRecipes.mvp.UsersRecipesView;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;

public class UsersRecipesActivity extends BaseActivity {

    public static final String USER = "USER_FOR_RECIPES";

    @Inject
    UsersRecipesView view;

    @Inject
    UsersRecipesPresenter presenter;

    public static void start(Context context, User user) {
        Intent intent = new Intent(context, UsersRecipesActivity.class);
        intent.putExtra(USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerUsersRecipesComponent.builder()
                .usersRecipesModule(new UsersRecipesModule(this))
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
