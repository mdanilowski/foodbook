package pl.mdanilowski.foodbook.activity.followers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.followers.dagger.DaggerFollowersComponent;
import pl.mdanilowski.foodbook.activity.followers.dagger.FollowersModule;
import pl.mdanilowski.foodbook.activity.followers.mvp.FollowersPresenter;
import pl.mdanilowski.foodbook.activity.followers.mvp.FollowersView;
import pl.mdanilowski.foodbook.app.App;

public class FollowersActivity extends BaseActivity {

    private static final String PASSED_UID_KEY = "PASSED_UID_KEY";

    @Inject
    FollowersPresenter presenter;

    @Inject
    FollowersView view;

    public static void start(Context context, String uid){
        Intent intent = new Intent(context, FollowersActivity.class);
        intent.putExtra(PASSED_UID_KEY, uid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFollowersComponent.builder()
                .followersModule(new FollowersModule(this))
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
                .build()
                .inject(this);
        setContentView(view);
        String uidFromIntent = getIntent().getStringExtra(PASSED_UID_KEY);
        presenter.setUid(uidFromIntent);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}