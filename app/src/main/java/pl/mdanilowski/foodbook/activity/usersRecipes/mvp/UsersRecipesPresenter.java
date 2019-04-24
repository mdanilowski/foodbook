package pl.mdanilowski.foodbook.activity.usersRecipes.mvp;

import java.net.UnknownHostException;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Subscription;

public class UsersRecipesPresenter extends BasePresenter {

    private final UsersRecipesView view;
    private final UsersRecipesModel model;

    private User user = null;

    public UsersRecipesPresenter(UsersRecipesView view, UsersRecipesModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        user = model.getUserFromIntent();
        compositeSubscription.add(observeUserRecipes());
        view.setUserData(user);
        view.setListener(recipe -> model.startRecipeDetailsActivity(user.getUid(), recipe.getRid()));
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    Subscription observeUserRecipes() {
        return foodBookService.getUsersRecipesRealtime(user.getUid())
                .subscribe(documentChange -> {
                            Recipe recipe = documentChange.getDocument().toObject(Recipe.class);
                            recipe.setRid(documentChange.getDocument().getId());
                            switch (documentChange.getType()) {
                                case ADDED:
                                    view.addItemToAdapter(recipe);
                                    break;
                                case MODIFIED:
                                    view.updateItemInAdapter(recipe);
                                    break;
                                case REMOVED:
                                    view.deleteItemFromAdapter(recipe);
                                    break;
                            }
                        }
                        , throwable -> {
                            if (throwable instanceof UnknownHostException) {
                                InformationDialog informationDialog = InformationDialog.newInstance(view.getResources().getString(R.string.failed_loading),
                                        view.getResources().getString(R.string.check_connection_try_later));
                                informationDialog.show(model.activity.getSupportFragmentManager(), "fragment_alert");
                            }
                        });
    }
}
