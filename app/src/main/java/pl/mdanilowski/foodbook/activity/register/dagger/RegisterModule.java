package pl.mdanilowski.foodbook.activity.register.dagger;


import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.register.RegisterActivity;
import pl.mdanilowski.foodbook.activity.register.mvp.RegisterModel;
import pl.mdanilowski.foodbook.activity.register.mvp.RegisterPresenter;
import pl.mdanilowski.foodbook.activity.register.mvp.RegisterView;

@Module
public class RegisterModule {

    private RegisterActivity activity;

    public RegisterModule(RegisterActivity activity) {
        this.activity = activity;
    }

    @Provides
    @RegisterScope
    public RegisterActivity activity(){
        return activity;
    }

    @Provides
    @RegisterScope
    public RegisterModel model(){
        return new RegisterModel(activity);
    }

    @Provides
    @RegisterScope
    public RegisterView view(){
        return new RegisterView(activity);
    }

    @Provides
    @RegisterScope
    public RegisterPresenter presenter(RegisterView view, RegisterModel model){
        return new RegisterPresenter(view, model);
    }
}
