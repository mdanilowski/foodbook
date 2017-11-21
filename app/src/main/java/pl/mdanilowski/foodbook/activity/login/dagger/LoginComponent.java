package pl.mdanilowski.foodbook.activity.login.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.login.LoginActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = LoginModule.class, dependencies = FoodbookAppComponent.class)
@LoginScope
public interface LoginComponent {

    void inject(LoginActivity loginActivity);
}
