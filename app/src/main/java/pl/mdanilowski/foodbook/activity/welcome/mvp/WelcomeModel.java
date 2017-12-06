package pl.mdanilowski.foodbook.activity.welcome.mvp;


import pl.mdanilowski.foodbook.activity.login.LoginActivity;
import pl.mdanilowski.foodbook.activity.register.RegisterActivity;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;

public class WelcomeModel {

    private WelcomeActivity activity;

    public WelcomeModel(WelcomeActivity activity) {
        this.activity = activity;
    }

    public WelcomeActivity getActivity() {
        return activity;
    }

    void startLoginActivity() {
        LoginActivity.start(activity);
    }

    void startRegisterActivity() {
        RegisterActivity.start(activity);
    }
}
