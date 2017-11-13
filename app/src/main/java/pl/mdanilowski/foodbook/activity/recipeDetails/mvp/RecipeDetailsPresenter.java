package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.CommentsAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecipeDetailsPresenter extends BasePresenter {

    public static final String NO_USER = "no_user";

    FirebaseUser user;

    private RecipeDetailsView view;
    private RecipeDetailsModel model;

    Recipe recipe;
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
        recipe = model.getRecipeFromIntent();
        setRecipeContent();
        setComments();
        user = firebaseAuth.getCurrentUser();
        compositeSubscription.add(observeLikeClick());
    }

    private void setComments() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        view.rvComments.setLayoutManager(linearLayoutManager);
        view.rvComments.setAdapter(commentsAdapter);
        commentsAdapter.setComments(recipe.getComments());
    }

    private void setRecipeContent() {
        if (recipe.getPhotosUrls() != null) {
            view.setPagerImages(recipe.getPhotosUrls());
        }
        view.setTvRecipeName(recipe.getName());
        view.setTvDescription(recipe.getDescription());
        view.setTvIngredients(recipe.getIngredients());
        view.setTvTags(recipe.getTags());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    Subscription observeLikeClick() {
        return view.likeClick().observeOn(Schedulers.io())
                .switchMap(code -> foodBookService.likeRecipe(user.getUid(), recipe.getOid(), recipe))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(code -> {
                    Toast.makeText(view.getContext(), "CODE: " + code, Toast.LENGTH_SHORT).show();
                    Log.d("LIKE", String.valueOf(code));
                }, Throwable::printStackTrace);
    }
}
