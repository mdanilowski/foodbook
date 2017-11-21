package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.CommentsAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Subscription;

public class RecipeDetailsPresenter extends BasePresenter {

    public static final String NO_USER = "no_user";

    FirebaseUser user;

    private RecipeDetailsView view;
    private RecipeDetailsModel model;

    private Recipe recipe;
    private User foodbookUser;

    CommentsAdapter commentsAdapter = new CommentsAdapter(uid ->
            foodBookService.findUserByUid(uid).subscribe(retrievedUser -> model.startProfileActivity(retrievedUser),
                    __ -> InformationDialog.newInstance(view.getResources().getString(R.string.no_user), view.getResources().getString(R.string.no_user_message))
                            .show(model.activity.getSupportFragmentManager(), NO_USER))
    );

    public RecipeDetailsPresenter(RecipeDetailsView view, RecipeDetailsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        String userId = model.getUidFromIntent();
        String recipeId = model.getRidFromIntent();
        foodbookUser = foodBookSimpleStorage.getUser();
        setCommentsAdapter();
        user = firebaseAuth.getCurrentUser();
        compositeSubscription.add(observeRecipe(userId, recipeId));
        compositeSubscription.add(observeRecipesComments(userId, recipeId));
        compositeSubscription.add(observeLikeClick());
        compositeSubscription.add(observeUnlikeClick());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private void setRecipeContent() {
        if (recipe.getPhotosUrls() != null) {
            view.setPagerImages(recipe.getPhotosUrls());
        }
        view.setTvRecipeName(recipe.getName());
        view.setTvDescription(recipe.getDescription());
        view.setTvIngredients(recipe.getIngredients());
        view.setTvTags(recipe.getTags());
        view.setShareCount(recipe.getShares());
        view.setLikesCount(recipe.getLikes());
        if (recipe.getComments() == null) {
            view.setCommentsCount(0);
        } else {
            view.setCommentsCount(recipe.getComments().size());
        }
        if (foodbookUser.getLikedRecipes().contains(recipe.getRid())) {
            view.setRecipeLiked();
        } else view.setRecipeNotLiked();
    }

    private void setCommentsAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        view.rvComments.setLayoutManager(linearLayoutManager);
        view.rvComments.setAdapter(commentsAdapter);
    }

    private Subscription observeRecipesComments(String uid, String rid) {
        return foodBookService.getRecipesCommentsRealtime(uid, rid)
                .subscribe(documentChange -> {
                    Comment comment = documentChange.getDocument().toObject(Comment.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            commentsAdapter.commentAdded(comment);
                            recipe.getComments().add(comment);
                            setRecipeContent();
                            break;
                    }
                });
    }

    private Subscription observeRecipe(String uid, String rid) {
        return foodBookService.getUsersRecipeRealtime(uid, rid)
                .subscribe(recipeSnapshot -> {
                    recipe = recipeSnapshot;
                    setRecipeContent();
                });
    }

    private Subscription observeLikeClick() {
        return view.likeClick()
                .subscribe(code -> {
                    foodbookUser.getLikedRecipes().add(recipe.getRid());
                    foodBookSimpleStorage.saveUser(foodbookUser);
                    view.setRecipeLiked();
                    view.setLikesCount(recipe.getLikes() + 1);
                    observeRecipeLiked(user, recipe);
                }, Throwable::printStackTrace);
    }

    private Subscription observeUnlikeClick() {
        return view.unlikeClick()
                .subscribe(aVoid -> {
                    view.setRecipeNotLiked();
                    view.setLikesCount(recipe.getLikes());
                    foodbookUser.getLikedRecipes().remove(recipe.getRid());
                    foodBookSimpleStorage.saveUser(foodbookUser);
                    foodBookService.unlikeRecipe(user, recipe).subscribe(__ -> Log.i("RECIPE_UNLIKED", "RECIPE UNLIKED: " + recipe.getRid()), throwable -> {
                        view.setRecipeLiked();
                        view.setLikesCount(recipe.getLikes() + 1);
                        Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    });
                });
    }

    private Subscription observeRecipeLiked(FirebaseUser user, Recipe recipe) {
        return foodBookService.likeRecipe(user, recipe)
                .subscribe(__ -> {

                            Log.i("RECIPE_LIKED", "RECIPE LIKED: " + recipe.getRid());
                        }, throwable -> {
                            view.setRecipeNotLiked();
                            view.setLikesCount(recipe.getLikes());
                            Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
