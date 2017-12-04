package pl.mdanilowski.foodbook.activity.welcome.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;
import pl.mdanilowski.foodbook.activity.welcome.mvp.WelcomeModel;
import pl.mdanilowski.foodbook.activity.welcome.mvp.WelcomePresenter;
import pl.mdanilowski.foodbook.activity.welcome.mvp.WelcomeView;

@Module
public class WelcomeModule {

    private WelcomeActivity welcomeActivity;

    public WelcomeModule(WelcomeActivity welcomeActivity) {
        this.welcomeActivity = welcomeActivity;
    }

    @Provides
    @WelcomeScope
    public WelcomeActivity welcomeActivity() {
        return welcomeActivity;
    }

    @Provides
    @WelcomeScope
    public WelcomePresenter welcomePresenter(WelcomeView view, WelcomeModel model) {
        return new WelcomePresenter(view, model);
    }

    @Provides
    @WelcomeScope
    public WelcomeView welcomeView() {
        return new WelcomeView(welcomeActivity, welcomeActivity.getSupportFragmentManager());
    }

    @Provides
    @WelcomeScope
    public WelcomeModel welcomeModel() {
        return new WelcomeModel(welcomeActivity);
    }
}
