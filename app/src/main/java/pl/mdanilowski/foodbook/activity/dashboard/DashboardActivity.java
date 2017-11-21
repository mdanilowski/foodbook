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
import pl.mdanilowski.foodbook.fragment.dashboard.RecipeIdeasFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;
import pl.mdanilowski.foodbook.model.Recipe;

public class DashboardActivity extends BaseActivity implements
        SearchFragment.OnFragmentInteractionListener,
        RecipeIdeasFragment.OnFragmentInteractionListener {

    public static final String IS_RECIPE_ADDED = "recipe_added";
    public static final String IMAGES = "images";
    public static final String RECIPE = "recipe";

    @Inject
    DashboardView view;

    @Inject
    DashboardPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        presenter.onFragmentInteraction(uri);
    }
}
