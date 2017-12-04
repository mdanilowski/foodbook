package pl.mdanilowski.foodbook.activity.following.mvp;


import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import rx.Subscription;

public class FollowingPresenter extends BasePresenter {

    private FollowingView view;
    private FollowingModel model;
    private User foodbookUser;

    public FollowingPresenter(FollowingView view, FollowingModel model) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.view = view;
        this.model = model;
        foodbookUser = foodBookSimpleStorage.getUser();
    }

    @Override
    public void onCreate() {
        compositeSubscription.add(observeGetFollowing(foodbookUser.getUid()));
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeGetFollowing(String uid) {
        return foodBookService.getUsersFollowedByUser(uid).subscribe(users -> {
            view.setFollowingUsers(users);
        });
    }
}
