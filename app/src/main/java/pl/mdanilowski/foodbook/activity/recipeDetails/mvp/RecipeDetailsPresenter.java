package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.Date;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.CommentsAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.CommentDialog;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Subscription;

public class RecipeDetailsPresenter extends BasePresenter {

    private static final String NO_USER = "no_user";
    private static final String COMMENT_DIALOG = "comment_dialog";

    FirebaseUser user;

    private RecipeDetailsView view;
    private RecipeDetailsModel model;

    private Recipe recipe;
    private User foodbookUser;

    String ownerId = null;
    String recipeId = null;

    private Uri shareLinkUri = null;

    private CommentsAdapter commentsAdapter = new CommentsAdapter(uid ->
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
        ownerId = model.getOidFromIntent();
        recipeId = model.getRidFromIntent();
        foodbookUser = foodBookSimpleStorage.getUser();
        setCommentsAdapter();
        user = firebaseAuth.getCurrentUser();
        compositeSubscription.add(observeRecipe(ownerId, recipeId));
        compositeSubscription.add(observeRecipesComments(ownerId, recipeId));
        compositeSubscription.add(observeLikeClick());
        compositeSubscription.add(observeUnlikeClick());
        compositeSubscription.add(observeCommentClick());
        compositeSubscription.add(observeShareClick());
        compositeSubscription.add(observeRecipeOwner(ownerId));
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private void createDynamicLink(Recipe recipe) {
        DynamicLink.SocialMetaTagParameters.Builder socialMediaParams = new DynamicLink.SocialMetaTagParameters.Builder();
        socialMediaParams.setTitle("Check out this recipe in FOODBOOK!");
        socialMediaParams.setDescription(recipe.getName());

        if (recipeId != null || ownerId != null) {
            DynamicLink recipeShareLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(String.format("https://foodbook.com/%s/%s", ownerId, recipeId)))
                    .setDynamicLinkDomain("bqbx5.app.goo.gl")
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    .setSocialMetaTagParameters(socialMediaParams.build())
                    .buildDynamicLink();

            shareLinkUri = recipeShareLink.getUri();
        } else {
            shareLinkUri = null;
        }
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
                            view.setCommentsCount(recipe.getComments().size());
                            setRecipeContent();
                            break;
                    }
                });
    }

    private Subscription observeRecipe(String uid, String rid) {
        return foodBookService.getUsersRecipeRealtime(uid, rid)
                .subscribe(recipeSnapshot -> {
                    recipe = recipeSnapshot;
                    createDynamicLink(recipe);
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
                    foodBookService.unlikeRecipeTransaction(user, recipe).subscribe(__ -> Log.i("RECIPE_UNLIKED", "RECIPE UNLIKED: " + recipe.getRid()), throwable -> {
                        view.setRecipeLiked();
                        view.setLikesCount(recipe.getLikes() + 1);
                        Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    });
                });
    }

    private Subscription observeCommentClick() {
        return view.commentClick().subscribe(aVoid -> {
            InputMethodManager inputMethodManager = (InputMethodManager) model.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            CommentDialog commentDialog = CommentDialog.newInstance(new CommentDialog.ButtonDialogClickListener() {
                @Override
                public void onCommentClick(String commentText) {
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    Comment comment = new Comment();
                    comment.setAddDate(new Date());
                    comment.setAvatarUrl(foodbookUser.getAvatarUrl());
                    comment.setCommentText(commentText);
                    comment.setName(foodbookUser.getName());
                    comment.setUid(foodbookUser.getUid());
                    foodBookService.commentRecipe(comment, recipe).subscribe(__ -> Toast.makeText(view.getContext(), "Comment added", Toast.LENGTH_SHORT).show(),
                            e -> Toast.makeText(view.getContext(), "Comment not added. Try again later.", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onCancelClick() {
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            });

            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            commentDialog.show(model.activity.getSupportFragmentManager(), COMMENT_DIALOG);
        });
    }

    private Subscription observeShareClick() {
        return view.shareClick().subscribe(__ -> {
            Intent sendIntent = new Intent();
            String msg = "Check out this recipe on Foodbook ;)\n\n" + shareLinkUri;
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            model.startShareIntent(sendIntent);
            foodBookService.incrementShareCountTransaction(ownerId, recipeId)
                    .subscribe(newSharesCount -> view.updateSharesCount(newSharesCount),
                            throwable -> Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    private Subscription observeRecipeLiked(FirebaseUser user, Recipe recipe) {
        return foodBookService.likeRecipeTransaction(user, recipe)
                .subscribe(__ -> Log.i("RECIPE_LIKED", "RECIPE LIKED: " + recipe.getRid()),
                        throwable -> {
                            view.setRecipeNotLiked();
                            view.setLikesCount(recipe.getLikes());
                            Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                );
    }

    private Subscription observeRecipeOwner(String oid) {
        return foodBookService.findUserByUid(oid).subscribe(user -> {
            view.setOwnerAvatar(user.getAvatarUrl());
            view.setOwnerName(user.getName());
            view.setClickListenerOnOwner(__ -> model.startProfileActivity(user.getUid()));
        });
    }
}
