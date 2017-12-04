package pl.mdanilowski.foodbook.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.activity.welcome.dagger.DaggerWelcomeComponent;
import pl.mdanilowski.foodbook.activity.welcome.dagger.WelcomeModule;
import pl.mdanilowski.foodbook.activity.welcome.mvp.WelcomePresenter;
import pl.mdanilowski.foodbook.activity.welcome.mvp.WelcomeView;

public class WelcomeActivity extends FragmentActivity {

    @Inject
    WelcomeView view;

    @Inject
    WelcomePresenter presenter;

    public static void start(Context context){
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerWelcomeComponent.builder().welcomeModule(new WelcomeModule(this))
                .build().inject(this);
        setContentView(view);
        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
