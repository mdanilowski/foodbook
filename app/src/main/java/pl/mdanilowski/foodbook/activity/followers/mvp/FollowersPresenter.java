package pl.mdanilowski.foodbook.activity.followers.mvp;


import android.widget.Toast;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.FollowAdapter;
import pl.mdanilowski.foodbook.app.App;

public class FollowersPresenter extends BasePresenter {

    private final FollowersView view;
    private final FollowersModel model;

    String uid = null;

    public FollowersPresenter(FollowersView view, FollowersModel model) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.view = view;
        this.model = model;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public void onCreate() {
        FollowAdapter followersAdapter = new FollowAdapter(user -> model.startProfileActivity(user.getUid()));
        view.setAdapterForRecyclerView(followersAdapter);
        if (uid != null) {
            foodBookService.getUsersFollowers(uid).subscribe(followersAdapter::setFollowingUsers,
                    e -> Toast.makeText(view.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }
}