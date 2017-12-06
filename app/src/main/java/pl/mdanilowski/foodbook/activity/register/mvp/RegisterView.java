package pl.mdanilowski.foodbook.activity.register.mvp;


import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.register.RegisterActivity;
import rx.Observable;

public class RegisterView extends FrameLayout {

    @BindView(R.id.toolbarDefault) Toolbar toolbar;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.etEmail) TextInputEditText etEmail;
    @BindView(R.id.tilPassword) TextInputLayout tilPassword;
    @BindView(R.id.etPassword) TextInputEditText etPassword;
    @BindView(R.id.tilPasswordConfirm) TextInputLayout tilPasswordConfirm;
    @BindView(R.id.etPasswordConfirm) TextInputEditText etPasswordConfirm;
    @BindView(R.id.tilName) TextInputLayout tilName;
    @BindView(R.id.etName) TextInputEditText etName;
    @BindView(R.id.tilCountry) TextInputLayout tilCountry;
    @BindView(R.id.etCountry) TextInputEditText etCountry;
    @BindView(R.id.tilCity) TextInputLayout tilCity;
    @BindView(R.id.etCity) TextInputEditText etCity;
    @BindView(R.id.tilAboutMe) TextInputLayout tilAboutMe;
    @BindView(R.id.etAboutMe) TextInputEditText etAboutMe;
    @BindView(R.id.btnRegiter) Button btnRegister;

    public RegisterView(@NonNull RegisterActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_register, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(R.string.registration);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
    }

    Observable<Void> registerClick() {
        return RxView.clicks(btnRegister);
    }

    void displayInputEmptyError(TextInputLayout til) {
        til.setError("That field is required!");
    }

    void hideInputEmptyError(TextInputLayout til) {
        til.setErrorEnabled(false);
    }

    void showPasswordDoNotMatch() {
        tilPasswordConfirm.setError("Passwords must match!");
    }
}
