package pl.mdanilowski.foodbook.activity.splash.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.splash.SplashActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@SplashScope
@Component(modules = SplashModule.class, dependencies = FoodbookAppComponent.class)
public interface SplashComponent {

    void inject(SplashActivity splashActivity);
}
