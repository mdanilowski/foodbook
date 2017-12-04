package pl.mdanilowski.foodbook.fragment.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.HomeAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.userUpdates.MyFollowerLikes;
import pl.mdanilowski.foodbook.model.userUpdates.MyRecipeLike;
import pl.mdanilowski.foodbook.service.FoodBookService;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomeFragment extends Fragment {

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FoodBookService foodBookService;

    FirebaseUser currentUser;

    @BindView(R.id.rvHomeRecyclerView)
    RecyclerView rvHomeRecyclerView;

    HomeAdapter homeAdapter = new HomeAdapter();

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvHomeRecyclerView.setAdapter(homeAdapter);
        rvHomeRecyclerView.setLayoutManager(linearLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        compositeSubscription.add(observeFollowersNewRecipes());
        compositeSubscription.add(observeFollowersComments());
        compositeSubscription.add(observeFollowersLikes());
        compositeSubscription.add(observeMyRecipesLikes());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeSubscription.clear();
    }

    private Subscription observeMyRecipesLikes() {
        return foodBookService.getMyLikesRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    MyRecipeLike myRecipeLike = documentChange.getDocument().toObject(MyRecipeLike.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(myRecipeLike);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeMyRecipeLike(myRecipeLike);
                            break;
                    }
                });
    }

    private Subscription observeFollowersNewRecipes() {
        return foodBookService.getNewFollowersRecipesRealtime(currentUser.getUid())
                .subscribe(newFollowersRecipe -> {
                    homeAdapter.addUpdate(newFollowersRecipe);
                    new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                });
    }

    private Subscription observeFollowersComments() {
        return foodBookService.getNewFollowersCommentRealtime(currentUser.getUid())
                .subscribe(newFollowersComment -> {
                    homeAdapter.addUpdate(newFollowersComment);
                    new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                });
    }

    private Subscription observeFollowersLikes() {
        return foodBookService.getFollowersLikesRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(documentChange.getDocument().toObject(MyFollowerLikes.class));
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeFollowersLike(documentChange.getDocument().toObject(MyFollowerLikes.class));
                            break;
                    }
                });
    }

}
