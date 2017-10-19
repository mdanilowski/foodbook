package pl.mdanilowski.foodbook.activity.dashboard.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@DashboardScope
@Component(modules = DashboardModule.class, dependencies = FoodbookAppComponent.class)
public interface DashboardComponent {

    void inject(DashboardActivity activity);
}
