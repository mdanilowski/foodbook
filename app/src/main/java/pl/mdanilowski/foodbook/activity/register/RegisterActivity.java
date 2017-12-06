package pl.mdanilowski.foodbook.activity.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.register.dagger.DaggerRegisterComponent;
import pl.mdanilowski.foodbook.activity.register.dagger.RegisterModule;
import pl.mdanilowski.foodbook.activity.register.mvp.RegisterPresenter;
import pl.mdanilowski.foodbook.activity.register.mvp.RegisterView;
import pl.mdanilowski.foodbook.app.App;

public class RegisterActivity extends BaseActivity {

    @Inject
    RegisterView view;

    @Inject
    RegisterPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerRegisterComponent.builder()
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
