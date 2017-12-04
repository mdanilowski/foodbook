package pl.mdanilowski.foodbook.activity.likedRecipes.mvp;


import android.widget.Toast;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.RecipesAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;

public class LikedRecipesPresenter extends BasePresenter {

    private final LikedRecipesView view;
    private final LikedRecipesModel model;

    private RecipesAdapter likedRecipesAdapter;
    private User foodbookUser;

    public LikedRecipesPresenter(LikedRecipesView view, LikedRecipesModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        foodbookUser = foodBookSimpleStorage.getUser();
        likedRecipesAdapter = new RecipesAdapter(model.getActivity(), recipe -> model.startRecipeDetailsActivity(
                foodbookUser.getUid(),
                recipe.getRid()
        ));
        view.setAdapterForRecyclerView(likedRecipesAdapter);

        foodBookService.getUsersLikedRecipes(foodbookUser.getUid())
                .subscribe(recipes -> likedRecipesAdapter.setRecipes(recipes),
                        e -> Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }
}