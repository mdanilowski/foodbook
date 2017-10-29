package pl.mdanilowski.foodbook.service;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.ext.FirestoreConstants;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
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
                        .get()
                        .addOnSuccessListener(subscriber::onNext)
                        .addOnFailureListener(subscriber::onError));
    }

    public Observable<List<DocumentSnapshot>> getUsersByNameAndSurename(String... input) {
        return Observable.create(subscriber -> {
            Query query = firestore.collection(FirestoreConstants.USERS);
            query.whereEqualTo("name", input[0]);
            query.whereEqualTo("surename", input[1]);
            query.get()
                    .addOnSuccessListener(command -> subscriber.onNext(command.getDocuments()))
                    .addOnFailureListener(subscriber::onError);
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

    private CollectionReference getUsersRecipesEndpoint(String uid) {
        return firestore.collection(FirestoreConstants.USER_RECIPES).document(uid).collection(FirestoreConstants.RECIPES);
    }
}
