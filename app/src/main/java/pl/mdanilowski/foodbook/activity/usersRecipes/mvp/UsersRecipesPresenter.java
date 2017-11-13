package pl.mdanilowski.foodbook.activity.usersRecipes.mvp;

import java.net.UnknownHostException;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
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
        view.setListener(model::startRecipeDetailsActivity);
        view.setUserData(user);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    Subscription observeUserRecipes() {
        return foodBookService.getUsersRecipesRealtime(user.getUid())
                .subscribe(view::addItemToAdapter, throwable -> {
                    if (throwable instanceof UnknownHostException) {
                        InformationDialog informationDialog = InformationDialog.newInstance("Failed loading data", "Check network connection and try again");
                        informationDialog.show(model.activity.getSupportFragmentManager(), "fragment_alert");
                    }
                });
    }
}
