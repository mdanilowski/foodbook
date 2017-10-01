package pl.mdanilowski.foodbook.app;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {

    private static App applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        applicationInstance = this;
    }

    public static App getApplicationInstance() {
        return applicationInstance;
    }
}
