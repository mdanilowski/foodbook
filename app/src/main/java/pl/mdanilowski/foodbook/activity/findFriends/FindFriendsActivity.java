package pl.mdanilowski.foodbook.activity.findFriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.findFriends.dagger.DaggerFindFriendsComponent;
import pl.mdanilowski.foodbook.activity.findFriends.dagger.FindFriendsModule;
import pl.mdanilowski.foodbook.activity.findFriends.mvp.FindFriendsPresenter;
import pl.mdanilowski.foodbook.activity.findFriends.mvp.FindFriendsView;
import pl.mdanilowski.foodbook.app.App;

public class FindFriendsActivity extends BaseActivity {

    @Inject
    FindFriendsView view;

    @Inject
    FindFriendsPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, FindFriendsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerFindFriendsComponent.builder()
                .findFriendsModule(new FindFriendsModule(this))
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
    public boolean onCreateOptionsMenu(Menu menu) {
        view.onCreateOptionsMenu(menu, this);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        presenter.onNewIntent(intent);
    }
}
