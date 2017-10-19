package pl.mdanilowski.foodbook.activity.base;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter {

    @Inject
    public FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseFirestore firebaseFirestore;

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();


    public abstract void onCreate();
    public abstract void onDestroy();

}
