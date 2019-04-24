package pl.mdanilowski.foodbook.activity.settingsProfile.mvp;


import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.settingsProfile.ProfileSettingsActivity;
import pl.mdanilowski.foodbook.model.User;
import rx.Observable;

public class ProfileSettingsView extends FrameLayout {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.ivBackgroundImage) ImageView ivBackgroundImage;
    @BindView(R.id.ivAvatar) ImageView ivAvatarImage;
    @BindView(R.id.btnChangeBackground) Button btnChangeBackgroundImage;
    @BindView(R.id.btnChangeAvatar) Button btnChangeAvatar;
    @BindView(R.id.tilName) TextInputLayout tilName;
    @BindView(R.id.etName) TextInputEditText etName;
    @BindView(R.id.tilAboutMe) TextInputLayout tilAbountMe;
    @BindView(R.id.etAboutMe) TextInputEditText etAboutMe;
    @BindView(R.id.tilEmail) TextInputLayout tilEmail;
    @BindView(R.id.etEmail) TextInputEditText etEmail;
    @BindView(R.id.tilCountry) TextInputLayout tilCountry;
    @BindView(R.id.etCountry) TextInputEditText etCountry;
    @BindView(R.id.tilCity) TextInputLayout tilCity;
    @BindView(R.id.etCity) TextInputEditText etCity;
    @BindView(R.id.btnSaveSettings) Button btnSaveSettings;

    public ProfileSettingsView(@NonNull ProfileSettingsActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_profile_settings, this);
        ButterKnife.bind(this);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        tvToolbarTitle.setText(getResources().getString(R.string.profile_settings));
    }

    public void setUserContent(User user){
        if(user.getAvatarUrl() != null){
            Glide.with(this).load(user.getAvatarUrl()).into(ivAvatarImage);
        }

        if(user.getBackgroundImage() != null) {
            Glide.with(this).load(user.getBackgroundImage()).into(ivBackgroundImage);
        }

        etName.setText(user.getName());
        etAboutMe.setText(user.getAboutMe());
        etEmail.setText(user.getEmail());
        etCountry.setText(user.getCountry());
        etCity.setText(user.getCity());
    }

    Observable<Void> changeBackgroundClicks() {
        return RxView.clicks(btnChangeBackgroundImage);
    }

    Observable<Void> changeAvatarImageClick() {
        return RxView.clicks(btnChangeAvatar);
    }

    Observable<Void> saveSettingsClick() {
        return RxView.clicks(btnSaveSettings);
    }
}
