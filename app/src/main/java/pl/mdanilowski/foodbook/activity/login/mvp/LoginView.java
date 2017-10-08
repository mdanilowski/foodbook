package pl.mdanilowski.foodbook.activity.login.mvp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import rx.Observable;

public class LoginView extends FrameLayout {

    @BindView(R.id.btnSignInGoogle)
    LinearLayout btnSignInGoogle;

    @BindView(R.id.btnSignInFacebook)
    LinearLayout btnSignInFacebook;

    public LoginView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.activity_login, this);
        ButterKnife.bind(this);
    }

    protected Observable<Void> googleLoginClick(){
        return RxView.clicks(btnSignInGoogle);
    }

    protected Observable<Void> setUpFacebookSignInButton(){
        return RxView.clicks(btnSignInFacebook);
    }
}
