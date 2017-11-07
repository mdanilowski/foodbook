package pl.mdanilowski.foodbook.activity.dashboard.dagger;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    public DashboardModel model() {
        return new DashboardModel(activity);
    }

    @Provides
    @DashboardScope
    public DashboardView view() {
        return new DashboardView(activity);
    }

    @Provides
    @DashboardScope
    public DashboardPresenter presenter(DashboardModel model, DashboardView view, FirebaseUser user) {
        return new DashboardPresenter(model, view, user);
    }

    @Provides
    @DashboardScope
    public FirebaseUser user(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser();
    }
}
