package pl.mdanilowski.foodbook.activity.splash.mvp;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.splash.SplashActivity;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;

public class SplashPresenter extends BasePresenter {

    SplashActivity splashActivity;
    FirebaseAuth firebaseAuth;

    public SplashPresenter(SplashActivity splashActivity ,FirebaseAuth firebaseAuth) {
        this.splashActivity = splashActivity;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onCreate() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            WelcomeActivity.start(splashActivity);
            splashActivity.finish();
        } else {
            DashboardActivity.start(splashActivity);
            splashActivity.finish();
        }
    }

    @Override
    public void onDestroy() {

    }
}
