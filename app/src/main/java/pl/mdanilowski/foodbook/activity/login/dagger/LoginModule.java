package pl.mdanilowski.foodbook.activity.login.dagger;


import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.login.LoginActivity;
import pl.mdanilowski.foodbook.activity.login.mvp.LoginModel;
import pl.mdanilowski.foodbook.activity.login.mvp.LoginPresenter;
import pl.mdanilowski.foodbook.activity.login.mvp.LoginView;

@Module
public class LoginModule {

    private LoginActivity loginActivity;

    public LoginModule(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Provides
    @LoginScope
    public LoginActivity loginActivity() {
        return loginActivity;
    }

    @Provides
    @LoginScope
    public LoginView loginView(LoginActivity activity) {
        return new LoginView(activity);
    }

    @Provides
    @LoginScope
    public LoginPresenter loginPresenter(LoginView view, LoginModel model, FirebaseAuth firebaseAuth) {
        return new LoginPresenter(view, model, firebaseAuth);
    }

    @Provides
    @LoginScope
    public LoginModel loginModel() {
        return new LoginModel(loginActivity);
    }

}
