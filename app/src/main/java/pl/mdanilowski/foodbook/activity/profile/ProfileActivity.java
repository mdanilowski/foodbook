package pl.mdanilowski.foodbook.activity.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.profile.dagger.DaggerProfileComponent;
import pl.mdanilowski.foodbook.activity.profile.dagger.ProfileModule;
import pl.mdanilowski.foodbook.activity.profile.mvp.ProfilePresenter;
import pl.mdanilowski.foodbook.activity.profile.mvp.ProfileView;
import pl.mdanilowski.foodbook.app.App;

public class ProfileActivity extends AppCompatActivity {

    @Inject
    ProfileView view;

    @Inject
    ProfilePresenter presenter;

    public static final String USER_UID = "USER_UID";

    public static void start(Context context, String uid) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER_UID, uid);
        context.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerProfileComponent.builder().foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .profileModule(new ProfileModule(this)).build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }
}
