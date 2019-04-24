package pl.mdanilowski.foodbook.activity.following;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.following.dagger.DaggerFollowingComponent;
import pl.mdanilowski.foodbook.activity.following.dagger.FollowingModule;
import pl.mdanilowski.foodbook.activity.following.mvp.FollowingPresenter;
import pl.mdanilowski.foodbook.activity.following.mvp.FollowingView;
import pl.mdanilowski.foodbook.app.App;

public class FollowingActivity extends BaseActivity {

    @Inject
    FollowingPresenter presenter;

    @Inject
    FollowingView view;

    public static void start(Context context){
        context.startActivity(new Intent(context, FollowingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFollowingComponent.builder()
                .followingModule(new FollowingModule(this))
                .foodbookAppComponent(App.getApplicationInstance().getFoodbookAppComponent())
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
