package pl.mdanilowski.foodbook.service;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.model.userUpdates.NewFollowersComment;
import pl.mdanilowski.foodbook.model.userUpdates.NewFollowersRecipe;
import pl.mdanilowski.foodbook.utils.FirestoreConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class FoodBookService {

    @Inject
    FirebaseFirestore firestore;

    @Inject
    FoodBookApi foodBookApi;

    public FoodBookService() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
    }

    public FoodBookService(FoodBookApi api) {
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

    public Observable<List<User>> getUsersByNameAndSurename(String input) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USERS)
                    .whereEqualTo("name", input)
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

    public Observable<List<Recipe>> getRecipesByWords(String input) {
        return Observable.create(subscriber -> {
            String[] splittedStrings = input.split(" ");

            CollectionReference collectionReference = firestore.collection("query-collection-recipes");
            Query query = collectionReference.whereEqualTo("queryStrings." + splittedStrings[0], true);

            for (int i = 1; i < splittedStrings.length; i++) {
                collectionReference.whereEqualTo("queryStrings." + splittedStrings[i], true);
            }

            query.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<Recipe> recipes = task.getResult().toObjects(Recipe.class);
                            subscriber.onNext(recipes);
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

    public Observable<Void> addRecipeToQueryTable(Recipe recipe) {
        return Observable.create(subscriber ->
                firestore.collection("query-collection-recipes")
                        .document(recipe.getRid())
                        .set(recipe)
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

    public Observable<List<Comment>> getCommentsForRecipe(String uid, String rid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USER_RECIPES)
                        .document(uid)
                        .collection(FirestoreConstants.RECIPES)
                        .document(rid)
                        .collection(FirestoreConstants.COMMENTS)
                        .get()
                        .addOnSuccessListener(snapshot -> subscriber.onNext(snapshot.toObjects(Comment.class)))
                        .addOnFailureListener(subscriber::onError)
        );
    }

    // #################### BATCH WRITES ##############################

    public Observable<Void> followUser(User user, User currentUser) {
        return Observable.create(subscriber -> {
            WriteBatch batch = firestore.batch();

            DocumentReference dRef1 = firestore.collection(FirestoreConstants.USERS).document(user.getUid()).collection(FirestoreConstants.FOLLOWERS).document(currentUser.getUid());
            DocumentReference dRef2 = firestore.collection(FirestoreConstants.USERS).document(currentUser.getUid()).collection(FirestoreConstants.FOLLOWING).document(user.getUid());
            batch.set(dRef1, currentUser);
            batch.set(dRef2, user);
            batch.commit()
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<Void> likeRecipe(FirebaseUser currentUser, Recipe recipe) {
        return Observable.create(subscriber -> {

            DocumentReference dRef = firestore.collection(FirestoreConstants.USER_RECIPES).document(recipe.getOid()).collection(FirestoreConstants.RECIPES).document(recipe.getRid());
            CollectionReference cRef = firestore.collection(FirestoreConstants.USERS).document(currentUser.getUid()).collection(FirestoreConstants.LIKED_RECIPES);

            firestore.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(dRef);
                long newLikesCount = snapshot.getLong("likes") + 1;
                transaction.update(dRef, "likes", newLikesCount);
                return null;
            }).addOnSuccessListener(command ->
                    cRef.document(recipe.getRid()).set(recipe)
                            .addOnSuccessListener(subscriber::onNext)
                            .addOnFailureListener(subscriber::onError)
            ).addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<Void> unlikeRecipe(FirebaseUser currentUser, Recipe recipe) {
        return Observable.create(subscriber -> {

            DocumentReference dRef = firestore.collection(FirestoreConstants.USER_RECIPES).document(recipe.getOid()).collection(FirestoreConstants.RECIPES).document(recipe.getRid());
            CollectionReference cRef = firestore.collection(FirestoreConstants.USERS).document(currentUser.getUid()).collection(FirestoreConstants.LIKED_RECIPES);

            firestore.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(dRef);
                long newLikesCount = snapshot.getLong("likes") - 1;
                transaction.update(dRef, "likes", newLikesCount);
                return null;
            }).addOnSuccessListener(command ->
                    cRef.document(recipe.getRid())
                            .delete()
                            .addOnSuccessListener(subscriber::onNext)
                            .addOnFailureListener(subscriber::onError)
            ).addOnFailureListener(subscriber::onError);
        });
    }

    // ######################  REALTIME LISTENERS  ##########################

    public Observable<User> findUserByUid(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid)
                        .addSnapshotListener((documentSnapshot, e) -> {
                            if (e != null || !documentSnapshot.exists()) {
                                subscriber.onError(e);
                            }
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                user.setUid(documentSnapshot.getId());
                                subscriber.onNext(user);
                            }
                        }));
    }

    public Observable<DocumentChange> getUsersFollowedByUser(String uid) {
        return Observable.create(subscriber -> firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.FOLLOWING)
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

    public Observable<DocumentChange> getUsersFollowersRealtime(String uid) {
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

    public Observable<List<Recipe>> getUsersLikedRecipes(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.LIKED_RECIPES)
                        .get().addOnSuccessListener(snapshot ->
                        subscriber.onNext(snapshot.toObjects(Recipe.class))
                ).addOnFailureListener(Throwable::printStackTrace));
    }

    public Observable<DocumentChange> getUsersRecipesRealtime(String uid) {
        return Observable.create(subscriber ->
                getUsersRecipesEndpoint(uid)
                        .orderBy("addDate", Query.Direction.DESCENDING)
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

    public Observable<Recipe> getUsersRecipeRealtime(String uid, String rid) {
        return Observable.create(subscriber ->
                getUsersRecipesEndpoint(uid)
                        .document(rid)
                        .addSnapshotListener((snapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (snapshot.exists()) {
                                Recipe recipe = snapshot.toObject(Recipe.class);
                                recipe.setRid(snapshot.getId());
                                subscriber.onNext(recipe);
                            }
                        }));
    }

    public Observable<DocumentChange> getRecipesCommentsRealtime(String uid, String rid) {
        return Observable.create(subscriber -> {
            getUsersRecipesEndpoint(uid)
                    .document(rid)
                    .collection(FirestoreConstants.COMMENTS)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            subscriber.onError(e);
                        }
                        if (snapshot != null && !snapshot.isEmpty()) {
                            for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                subscriber.onNext(dc);
                            }
                        }
                    });
        });
    }

    public Observable<DocumentChange> getMyLikesRealtime(String uid) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USER_UPDATES)
                    .document(uid)
                    .collection(FirestoreConstants.MY_RECIPE_LIKES)
                    .orderBy("addDate", Query.Direction.DESCENDING)
                    .addSnapshotListener((snapshot, e) -> {
                        if (e != null) {
                            subscriber.onError(e);
                        }
                        if (snapshot != null && !snapshot.isEmpty()) {
                            for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                subscriber.onNext(dc);
                            }
                        }
                    });
        });
    }

    public Observable<NewFollowersRecipe> getNewFollowersRecipesRealtime(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USER_UPDATES)
                        .document(uid)
                        .collection(FirestoreConstants.FOLLOWERS_RECIPES)
                        .orderBy("addDate", Query.Direction.DESCENDING)
                        .addSnapshotListener((snapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (snapshot != null && !snapshot.isEmpty()) {
                                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                            subscriber.onNext(dc.getDocument().toObject(NewFollowersRecipe.class));
                                    }
                                }
                            }
                        })
        );
    }

    public Observable<NewFollowersComment> getNewFollowersCommentRealtime(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USER_UPDATES)
                        .document(uid)
                        .collection(FirestoreConstants.FOLLOWERS_COMMENTS)
                        .orderBy("addDate", Query.Direction.DESCENDING)
                        .addSnapshotListener((snapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (snapshot != null && !snapshot.isEmpty()) {
                                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                            subscriber.onNext(dc.getDocument().toObject(NewFollowersComment.class));
                                    }
                                }
                            }
                        }));
    }

    public Observable<DocumentChange> getFollowersLikesRealtime(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USER_UPDATES)
                        .document(uid)
                        .collection(FirestoreConstants.FOLLOWERS_LIKES)
                        .orderBy("addDate", Query.Direction.DESCENDING)
                        .addSnapshotListener((snapshot, e) -> {
                            if (e != null) {
                                subscriber.onError(e);
                            }
                            if (snapshot != null && !snapshot.isEmpty()) {
                                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                    subscriber.onNext(dc);
                                }
                            }
                        })
        );
    }

    // ######################  ENDPOINT DECLARATION  ##########################

    private CollectionReference getUsersRecipesEndpoint(String uid) {
        return firestore.collection(FirestoreConstants.USER_RECIPES).document(uid).collection(FirestoreConstants.RECIPES);
    }

    //  ####################### HTTP CALLS ###########################

    public Observable<Integer> likeRecipe(String uid, String oid, Recipe recipe) {
        return Observable.create(subscriber ->
                foodBookApi.recipeLiked(uid, oid, recipe).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        subscriber.onNext(response.code());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        subscriber.onError(t);
                    }
                }));
    }
}
