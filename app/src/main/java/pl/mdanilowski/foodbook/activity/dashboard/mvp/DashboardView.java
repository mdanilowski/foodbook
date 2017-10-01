package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;

@SuppressLint("ViewConstructor")
public class DashboardView extends FrameLayout {

    public DashboardView(@NonNull DashboardActivity dashboardActivity) {
        super(dashboardActivity);

        inflate(dashboardActivity, R.layout.activity_dashboard, this);
        ButterKnife.bind(this);
    }
}