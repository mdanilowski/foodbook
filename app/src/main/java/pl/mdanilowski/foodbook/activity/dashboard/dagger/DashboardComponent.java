package pl.mdanilowski.foodbook.activity.dashboard.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;

@DashboardScope
@Component(modules = DashboardModule.class)
public interface DashboardComponent {

    void inject(DashboardActivity activity);
}
