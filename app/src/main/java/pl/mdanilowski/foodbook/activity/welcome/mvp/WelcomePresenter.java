package pl.mdanilowski.foodbook.activity.welcome.mvp;


import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import rx.Subscription;

public class WelcomePresenter extends BasePresenter {

    private WelcomeView view;
    private WelcomeModel model;

    public WelcomePresenter(WelcomeView view, WelcomeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        compositeSubscription.add(observeLoginRequestButtonClick());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeLoginRequestButtonClick(){
        return view.loginButtonClick().subscribe(__ -> model.startLoginActivity());
    }
}
