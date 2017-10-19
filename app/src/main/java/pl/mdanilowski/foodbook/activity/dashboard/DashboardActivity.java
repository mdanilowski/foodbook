package pl.mdanilowski.foodbook.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DaggerDashboardComponent;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DashboardModule;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardPresenter;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardView;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.fragment.dashboard.HomeFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipeIdeasFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;

public class DashboardActivity extends BaseActivity implements RecipesFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        SearchFragment.OnFragmentInteractionListener,
        RecipeIdeasFragment.OnFragmentInteractionListener {

    @Inject
    DashboardView view;

    @Inject
    DashboardPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        presenter.onFragmentInteraction(uri);
    }
}
