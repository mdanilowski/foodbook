package pl.mdanilowski.foodbook.activity.welcome.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;

@Component(modules = WelcomeModule.class)
@WelcomeScope
public interface WelcomeComponent {

    void inject(WelcomeActivity welcomeActivity);
}
