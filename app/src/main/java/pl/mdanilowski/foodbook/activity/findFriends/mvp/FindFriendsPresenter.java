package pl.mdanilowski.foodbook.activity.findFriends.mvp;

import android.app.SearchManager;
import android.content.Intent;
import android.widget.Toast;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.UserAdapter;
import pl.mdanilowski.foodbook.app.App;

public class FindFriendsPresenter extends BasePresenter {
    private final FindFriendsView view;
    private final FindFriendsModel model;

    private UserAdapter userAdapter;

    public FindFriendsPresenter(FindFriendsView view, FindFriendsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        userAdapter = new UserAdapter(user -> model.startProfileActivity(user.getUid()));
        view.setAdapterForRecyclerView(userAdapter);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    public void onNewIntent(Intent intent) {
        model.getActivity().setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        if (model.getIsNewIntentSearch()) {
            String searchQuery = model.getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            runQueryForUsers(searchQuery);
        }
    }

    private void runQueryForUsers(String input) {
        foodBookService.findUsers(input).subscribe(users -> userAdapter.setUsers(users),
                e -> Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show());
    }
}
