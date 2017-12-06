package pl.mdanilowski.foodbook.activity.login.mvp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import rx.Observable;

public class LoginView extends FrameLayout {

    @BindView(R.id.btnSignInGoogle) LinearLayout btnSignInGoogle;
    @BindView(R.id.btnSignInFacebook) LinearLayout btnSignInFacebook;
    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.etEmail) TextInputEditText etEmail;
    @BindView(R.id.tilPassword) TextInputLayout tilPassword;
    @BindView(R.id.etPassword) TextInputEditText etPassword;
    @BindView(R.id.btnLogin) Button btnLogin;

    public LoginView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.activity_login, this);
        ButterKnife.bind(this);
    }

    protected Observable<Void> googleLoginClick() {
        return RxView.clicks(btnSignInGoogle);
    }

    protected Observable<Void> setUpFacebookSignInButton() {
        return RxView.clicks(btnSignInFacebook);
    }

    protected Observable<Void> loginClicked() {
        return RxView.clicks(btnLogin);
    }

    void displayInputEmptyError(TextInputLayout til) {
        til.setError("That field is required!");
    }

    void hideInputEmptyError(TextInputLayout til) {
        til.setErrorEnabled(false);
    }
}
