package pl.mdanilowski.foodbook.activity.settingsProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.settingsProfile.dagger.DaggerProfileSettingsComponent;
import pl.mdanilowski.foodbook.activity.settingsProfile.dagger.ProfileSettingsModule;
import pl.mdanilowski.foodbook.activity.settingsProfile.mvp.ProfileSettingsPresenter;
import pl.mdanilowski.foodbook.activity.settingsProfile.mvp.ProfileSettingsView;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;

public class ProfileSettingsActivity extends BaseActivity {

    public static final String USER = "USER";

    @Inject
    ProfileSettingsPresenter presenter;

    @Inject
    ProfileSettingsView view;

    public static void start(Context context, User foodbookUser){
        Intent intent = new Intent(context, ProfileSettingsActivity.class);
        intent.putExtra(USER, foodbookUser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerProfileSettingsComponent.builder()
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .profileSettingsModule(new ProfileSettingsModule(this))
                .build().inject(this);
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
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
