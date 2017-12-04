package pl.mdanilowski.foodbook.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DaggerDashboardComponent;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DashboardModule;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardPresenter;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardView;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;

public class DashboardActivity extends BaseActivity {

    public static final String IS_RECIPE_ADDED = "recipe_added";
    public static final String IMAGES = "images";
    public static final String RECIPE = "recipe";

    public static final String IS_USER_UPDATED = "is_user_updated";
    public static final String AVATAR_URI = "avatar_uri";
    public static final String BACKGROUND_URI = "background_uri";
    public static final String USER_UPDATED = "user_updated";

    @Inject
    DashboardView view;

    @Inject
    DashboardPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void start(Context context, User foodbookUser, Uri avatar, Uri background){
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(IS_USER_UPDATED, true);
        intent.putExtra(AVATAR_URI, avatar);
        intent.putExtra(BACKGROUND_URI, background);
        intent.putExtra(USER_UPDATED, foodbookUser);
        context.startActivity(intent);
    }

    public static void start(Context context, List<Uri> images, Recipe recipe, boolean wasRecipaAdded) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(IS_RECIPE_ADDED, wasRecipaAdded);
        intent.putParcelableArrayListExtra(IMAGES, (ArrayList<? extends Parcelable>) images);
        intent.putExtra(RECIPE, recipe);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerDashboardComponent.builder().foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .dashboardModule(new DashboardModule(this))
                .build().inject(this);

        setContentView(view);
        presenter.onCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        view.onCreateOptionsMenu(menu, this);
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        presenter.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public DashboardPresenter getPresenter() {
        return presenter;
    }

}
