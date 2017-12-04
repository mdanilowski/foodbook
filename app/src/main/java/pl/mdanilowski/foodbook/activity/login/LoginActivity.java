package pl.mdanilowski.foodbook.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.login.dagger.DaggerLoginComponent;
import pl.mdanilowski.foodbook.activity.login.dagger.LoginModule;
import pl.mdanilowski.foodbook.activity.login.mvp.LoginPresenter;
import pl.mdanilowski.foodbook.activity.login.mvp.LoginView;
import pl.mdanilowski.foodbook.app.App;

public class LoginActivity extends AppCompatActivity {

    @Inject
    LoginView view;

    @Inject
    LoginPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerLoginComponent.builder()
                .loginModule(new LoginModule(this))
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginPresenter.RC_SIGN_IN) {
            presenter.onSignInGoogleActivityResult(requestCode, data);
        } else {
            presenter.onFacebookLoginActivityResult(requestCode, resultCode, data);
        }

    }
}
