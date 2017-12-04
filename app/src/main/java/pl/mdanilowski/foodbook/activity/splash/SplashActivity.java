package pl.mdanilowski.foodbook.activity.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.splash.dagger.DaggerSplashComponent;
import pl.mdanilowski.foodbook.activity.splash.dagger.SplashModule;
import pl.mdanilowski.foodbook.activity.splash.mvp.SplashPresenter;
import pl.mdanilowski.foodbook.activity.splash.mvp.SplashView;
import pl.mdanilowski.foodbook.app.App;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SplashView view;

    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSplashComponent.builder().splashModule(new SplashModule(this))
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .build().inject(this);

        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
