package pl.mdanilowski.foodbook.activity.register.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.register.RegisterActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = RegisterModule.class, dependencies = FoodbookAppComponent.class)
@RegisterScope
public interface RegisterComponent {

    void inject(RegisterActivity activity);
}
