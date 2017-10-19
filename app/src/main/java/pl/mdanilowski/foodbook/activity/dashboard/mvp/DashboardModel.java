package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.support.v4.app.FragmentManager;

import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;

public class DashboardModel {

    DashboardActivity activity;

    public DashboardModel(DashboardActivity activity) {
        this.activity = activity;
    }

    public DashboardActivity getActivity() {
        return activity;
    }

    public FragmentManager getFragmentManager(){
        return activity.getSupportFragmentManager();
    }
}

