package pl.mdanilowski.foodbook.activity.splash.dagger;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.splash.SplashActivity;
import pl.mdanilowski.foodbook.activity.splash.mvp.SplashModel;
import pl.mdanilowski.foodbook.activity.splash.mvp.SplashPresenter;
import pl.mdanilowski.foodbook.activity.splash.mvp.SplashView;

@Module
public class SplashModule {

    private SplashActivity splashActivity;

    public SplashModule(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    @Provides
    @SplashScope
    public SplashActivity splashActivity(){
        return splashActivity;
    }

    @Provides
    @SplashScope
    public SplashView splashView(){
        return new SplashView(splashActivity);
    }

    @Provides
    @SplashScope
    public SplashPresenter splashPresenter(SplashView view, SplashModel model, FirebaseAuth firebaseAuth){
        return new SplashPresenter(view, model, firebaseAuth);
    }

    @Provides
    @SplashScope
    public SplashModel model(){
        return new SplashModel(splashActivity);
    }

}
