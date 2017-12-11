package pl.mdanilowski.foodbook.service;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
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

    public Observable<User> getUser(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid)
                        .get().addOnSuccessListener(documentSnapshot -> {
                    subscriber.onNext(documentSnapshot.toObject(User.class));
                })
                        .addOnFailureListener(subscriber::onError));
    }

    public Observable<Void> setUserSettings(User user) {
        return Observable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", user.getName());
            map.put("aboutMe", user.getAboutMe());
            map.put("email", user.getEmail());
            map.put("country", user.getCountry());
            map.put("city", user.getCity());
            firestore.collection(FirestoreConstants.USERS)
                    .document(user.getUid())
                    .set(map, SetOptions.merge())
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<Void> updateUsersAvatarPhoto(String uid, String photoUrl) {
        return Observable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put("avatarUrl", photoUrl);
            firestore.collection(FirestoreConstants.USERS).document(uid).set(map, SetOptions.merge())
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<Void> updateUsersBackgroundPhoto(String uid, String photoUrl) {
        return Observable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put("backgroundImage", photoUrl);
            firestore.collection(FirestoreConstants.USERS).document(uid).set(map, SetOptions.merge())
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    public Observable<DocumentReference> addRecipeToUser(String uid, Recipe recipe) {
        return Observable.create(subscriber -> getUsersRecipesEndpoint(uid)
                .add(recipe)
                .addOnSuccessListener(subscriber::onNext)
                .addOnFailureListener(subscriber::onError));
    }

    public Observable<DocumentReference> commentRecipe(Comment comment, Recipe recipe) {
        return Observable.create(subscriber -> {
            firestore.collection(FirestoreConstants.USER_RECIPES)
                    .document(recipe.getOid())
                    .collection(FirestoreConstants.RECIPES)
                    .document(recipe.getRid())
                    .collection(FirestoreConstants.COMMENTS)
                    .add(comment)
                    .addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
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

    public Observable<List<User>> getUsersFollowedByUser(String uid) {
        return Observable.create(subscriber -> firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.FOLLOWING)
                .get()
                .addOnSuccessListener(snapshot -> subscriber.onNext(snapshot.toObjects(User.class)))
                .addOnFailureListener(subscriber::onError));
    }

    public Observable<List<User>> getUsersFollowers(String uid) {
        return Observable.create(subscriber -> firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.FOLLOWERS)
                .get()
                .addOnSuccessListener(snapshot -> subscriber.onNext(snapshot.toObjects(User.class)))
                .addOnFailureListener(subscriber::onError));
    }

    public Observable<List<Recipe>> getUsersLikedRecipes(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid).collection(FirestoreConstants.LIKED_RECIPES)
                        .get().addOnSuccessListener(snapshot ->
                        subscriber.onNext(snapshot.toObjects(Recipe.class))
                ).addOnFailureListener(Throwable::printStackTrace));
    }

    // #################### BATCH WRITES ##############################

    public Observable<Void> followUserBatch(User user, User currentUser) {
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

    public Observable<Void> likeRecipeTransactionBatch(FirebaseUser currentUser, Recipe recipe) {
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

    // ********************* TRANSACTIONS **************************

    public Observable<Void> unlikeRecipeTransaction(FirebaseUser currentUser, Recipe recipe) {
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

    public Observable<Long> incrementShareCountTransaction(String oid, String rid) {
        return Observable.create(subscriber -> {
            DocumentReference dRef = firestore.collection(FirestoreConstants.USER_RECIPES)
                    .document(oid)
                    .collection(FirestoreConstants.RECIPES)
                    .document(rid);
            firestore.runTransaction(transaction -> {
                DocumentSnapshot documentSnapshot = transaction.get(dRef);
                long newSharesCount = documentSnapshot.getLong("shares") + 1;
                transaction.update(dRef, "shares", newSharesCount);
                return newSharesCount;
            }).addOnSuccessListener(subscriber::onNext)
                    .addOnFailureListener(subscriber::onError);
        });
    }

    // ######################  REALTIME LISTENERS  ##########################

    public Observable<User> getUserRealtime(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USERS).document(uid)
                        .addSnapshotListener((documentSnapshot, e) -> {
                            if (!documentSnapshot.exists()) {
                                subscriber.onError(e);
                            }
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                user.setUid(documentSnapshot.getId());
                                subscriber.onNext(user);
                            }
                        }));
    }

    public Observable<DocumentChange> getUsersFollowedByUserRealtime(String uid) {
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

    // ***************************  MESSAGE WALL  ***************************

    public Observable<DocumentChange> getMyLikesRealtime(String uid) {
        return Observable.create(subscriber ->
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
                        })
        );
    }

    public Observable<DocumentChange> getNewFollowersRecipesRealtime(String uid) {
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
                                    subscriber.onNext(dc);
                                }
                            }
                        })
        );
    }

    public Observable<DocumentChange> getNewFollowersCommentRealtime(String uid) {
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
                                    subscriber.onNext(dc);
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

    public Observable<DocumentChange> getMyRecipesCommentsRealtime(String uid) {
        return Observable.create(subscriber ->
                firestore.collection(FirestoreConstants.USER_UPDATES)
                        .document(uid)
                        .collection(FirestoreConstants.MY_RECIPE_COMMENT)
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

    public Observable<Integer> likeRecipeTransactionBatch(String uid, String oid, Recipe recipe) {
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

    // ************************ SEARCH QUERIES ***************************************

    public Observable<List<Recipe>> findRecipesByWords(String input) {
        return Observable.create(subscriber -> {
            String[] splittedStrings = input.split(" ");

            CollectionReference collectionReference = firestore.collection("query-collection-recipes");
            Query query = collectionReference.whereEqualTo("queryStrings." + splittedStrings[0].toLowerCase(), true);

            for (int i = 1; i < splittedStrings.length; i++) {
                collectionReference.whereEqualTo("queryStrings." + splittedStrings[i].toLowerCase(), true);
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

    public Observable<List<User>> findUsers(String input) {
        return Observable.create(subscriber -> {
            String[] splittedStrings = input.split(" ");

            CollectionReference collectionReference = firestore.collection(FirestoreConstants.USERS);
            Query query = collectionReference.whereEqualTo("queryMap." + splittedStrings[0].toLowerCase(), true);

            for (int i = 1; i < splittedStrings.length; i++) {
                collectionReference.whereEqualTo("queryMap." + splittedStrings[i].toLowerCase(), true);
            }

            query.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<User> queriedUsers = task.getResult().toObjects(User.class);
                            subscriber.onNext(queriedUsers);
                        } else {
                            subscriber.onError(task.getException());
                        }
                    });
        });
    }
}
