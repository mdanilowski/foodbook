package pl.mdanilowski.foodbook.activity.base;


import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter {
    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    public abstract void onCreate();
    public abstract void onDestroy();

}
