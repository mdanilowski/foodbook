package pl.mdanilowski.foodbook.activity.splash.mvp;


import android.content.Intent;

import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.splash.SplashActivity;
import pl.mdanilowski.foodbook.activity.welcome.WelcomeActivity;

public class SplashModel {
    private SplashActivity splashActivity;

    public SplashModel(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    void startDashboardWithDeepLink(String uid, String rid) {
        DashboardActivity.start(splashActivity, true, uid, rid);
        splashActivity.finish();
    }

    void startDashboardActivity() {
        DashboardActivity.start(splashActivity);
        splashActivity.finish();
    }

    void startWelcomeActivity() {
        WelcomeActivity.start(splashActivity);
        splashActivity.finish();
    }

    Intent getIntent() {
        return splashActivity.getIntent();
    }
}
