package pl.mdanilowski.foodbook.service;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.FirestoreConstants;
import rx.Observable;

public class FoodBookService {

    @Inject
    FirebaseFirestore firestore;

    public FoodBookService() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
    }

    public Observable<Void> setUser(String uid, User user) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS)
                        .document(uid)
                        .set(user)
                        .addOnSuccessListener(subscriber::onNext)
                        .addOnFailureListener(subscriber::onError));
    }

    public Observable<DocumentSnapshot> findUserByUid(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid)
                        .addSnapshotListener((documentSnapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (documentSnapshot != null) {
                                subscriber.onNext(documentSnapshot);
                            }
                        }));
    }

    public Observable<List<User>> getUsersByNameAndSurename(String... input) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USERS)
                    .whereEqualTo("name", input[0])
                    .whereEqualTo("surename", input[1])
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<User> users = new ArrayList<>();
                            User user;
                            for (DocumentSnapshot ds : task.getResult().getDocuments()) {
                                user = ds.toObject(User.class);
                                user.setUid(ds.getId());
                                users.add(user);
                            }
                            subscriber.onNext(users);
                        } else {
                            subscriber.onError(task.getException());
                        }
                    });
        });
    }

    public Observable<DocumentReference> addRecipeToUser(String uid, Recipe recipe) {
        return Observable.create(subscriber -> getUsersRecipesEndpoint(uid)
                .add(recipe)
                .addOnSuccessListener(subscriber::onNext)
                .addOnFailureListener(subscriber::onError));
    }

    public Observable<List<DocumentSnapshot>> getUsersRecipes(String uid) {
        return Observable.create(subscriber ->
                getUsersRecipesEndpoint(uid)
                        .orderBy("addDate", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener(runnable -> subscriber.onNext(runnable.getDocuments()))
                        .addOnFailureListener(subscriber::onError));
    }

    public Observable<Recipe> getUsersRecipesRealtime(String uid) {
        return Observable.create(subscriber ->
                getUsersRecipesEndpoint(uid)
                        .orderBy("addDate", Query.Direction.ASCENDING)
                        .addSnapshotListener((snapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (snapshot != null && !snapshot.isEmpty()) {
                                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                            subscriber.onNext(dc.getDocument().toObject(Recipe.class));
                                    }
                                }

                            }
                        }));
    }

    public Observable<Void> followUser(User user, User currentUser) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USERS).document(user.getUid()).collection(FirestoreConstants.FOLLOWERS).document(currentUser.getUid())
                    .set(currentUser).addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<Void> setFollowingUser(User user, User currentUser) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USERS).document(currentUser.getUid()).collection(FirestoreConstants.FOLLOWING).document(user.getUid())
                    .set(user).addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<DocumentChange> getUsersFriends(String uid) {
        return Observable.create(subscriber -> firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.FOLLOWERS)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        subscriber.onError(e);
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            subscriber.onNext(dc);
                        }
                    }
                }));
    }

    private CollectionReference getUsersRecipesEndpoint(String uid) {
        return firestore.collection(FirestoreConstants.USER_RECIPES).document(uid).collection(FirestoreConstants.RECIPES);
    }
}
