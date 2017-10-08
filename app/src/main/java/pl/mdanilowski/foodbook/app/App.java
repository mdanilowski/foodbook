package pl.mdanilowski.foodbook.app;

import android.app.Application;

import pl.mdanilowski.foodbook.app.dagger.DaggerFoodbookAppComponent;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;
import timber.log.Timber;

public class App extends Application {

    private static App applicationInstance;
    private FoodbookAppComponent foodbookAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        applicationInstance = this;
        foodbookAppComponent = DaggerFoodbookAppComponent.builder().build();
    }

    public static App getApplicationInstance() {
        return applicationInstance;
    }

    public FoodbookAppComponent getFoodbookAppComponent() {
        return foodbookAppComponent;
    }
}
