package pl.mdanilowski.foodbook.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DaggerDashboardComponent;
import pl.mdanilowski.foodbook.activity.dashboard.dagger.DashboardModule;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardPresenter;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardView;

public class DashboardActivity extends BaseActivity {

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

        DaggerDashboardComponent.builder().dashboardModule(new DashboardModule(this))
                .build().inject(this);

        setContentView(R.layout.activity_dashboard);
    }
}
