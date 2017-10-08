package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.widget.Toast;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;

public class DashboardPresenter extends BasePresenter {

    private DashboardView view;
    private DashboardModel model;

    public DashboardPresenter(DashboardModel model, DashboardView view) {
    }

    @Override
    public void onCreate() {
        Toast.makeText(model.activity, "Dashboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {

    }
}
