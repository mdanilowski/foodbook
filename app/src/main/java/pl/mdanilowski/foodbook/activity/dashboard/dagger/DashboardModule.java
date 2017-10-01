package pl.mdanilowski.foodbook.activity.dashboard.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardModel;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardPresenter;
import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardView;

@Module
public class DashboardModule {

    private DashboardActivity activity;

    public DashboardModule(DashboardActivity activity) {
        this.activity = activity;
    }

    @Provides
    @DashboardScope
    public DashboardActivity activity() {
        return activity;
    }

    @Provides
    @DashboardScope
    public DashboardModel model(DashboardActivity activity) {
        return new DashboardModel(activity);
    }

    @Provides
    @DashboardScope
    public DashboardView view(DashboardActivity dashboardActivity) {
        return new DashboardView(dashboardActivity);
    }

    @Provides
    @DashboardScope
    public DashboardPresenter presenter(DashboardModel model, DashboardView view) {
        return new DashboardPresenter(model, view);
    }

}
