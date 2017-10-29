package pl.mdanilowski.foodbook.activity.base;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.service.FoodBookService;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter {

    @Inject
    public FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseFirestore firebaseFirestore;

    @Inject
    public FirebaseStorage storage;

    @Inject
    public FoodBookService foodBookService;

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();


    public abstract void onCreate();
    public abstract void onDestroy();

}
