package pl.mdanilowski.foodbook.activity.base;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;
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

    @Inject
    public FoodBookSimpleStorage foodBookSimpleStorage;

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    public abstract void onCreate();
    public abstract void onDestroy();
}
