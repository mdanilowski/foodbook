package pl.mdanilowski.foodbook.fragment.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.HomeAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.userUpdates.UpdateType;
import pl.mdanilowski.foodbook.model.userUpdates.UserUpdate;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomeFragment extends Fragment {

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FoodBookService foodBookService;

    @Inject
    FoodBookSimpleStorage foodBookSimpleStorage;

    FirebaseUser currentUser;

    @BindView(R.id.rvHomeRecyclerView)
    RecyclerView rvHomeRecyclerView;

    private HomeAdapter homeAdapter;

    private int notificationIdCounter = 0;

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
        homeAdapter = new HomeAdapter(this.getContext(), update -> RecipeDetailsActivity.start(this.getContext(), update.getOid(), update.getRid()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        rvHomeRecyclerView.setAdapter(homeAdapter);
        rvHomeRecyclerView.setLayoutManager(linearLayoutManager);
        rvHomeRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        compositeSubscription.add(observeFollowersNewRecipes());
        compositeSubscription.add(observeFollowersComments());
        compositeSubscription.add(observeFollowersLikes());
        compositeSubscription.add(observeMyRecipesLikes());
        compositeSubscription.add(observeMyRecipesComments());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeSubscription.clear();
    }

    private Subscription observeMyRecipesLikes() {
        return foodBookService.getMyLikesRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    UserUpdate myRecipeLike = documentChange.getDocument().toObject(UserUpdate.class);
                    myRecipeLike.setUpdateType(UpdateType.MY_RECIPE_LIKE);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(myRecipeLike);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeUpdate(myRecipeLike);
                            break;
                        case MODIFIED:
                            break;
                        default:
                            break;
                    }
                });
    }

    private Subscription observeFollowersNewRecipes() {
        return foodBookService.getNewFollowersRecipesRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    UserUpdate newFollowersRecipe = documentChange.getDocument().toObject(UserUpdate.class);
                    newFollowersRecipe.setUpdateType(UpdateType.FOLLOWERS_NEW_RECIPE);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(newFollowersRecipe);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeUpdate(newFollowersRecipe);
                            break;
                        case MODIFIED:
                            break;
                        default:
                            break;
                    }
                });
    }

    private Subscription observeFollowersComments() {
        return foodBookService.getNewFollowersCommentRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    UserUpdate newFollowersComment = documentChange.getDocument().toObject(UserUpdate.class);
                    newFollowersComment.setUpdateType(UpdateType.FOLLOWERS_NEW_COMMENT);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(newFollowersComment);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeUpdate(newFollowersComment);
                            break;
                        case MODIFIED:
                            break;
                        default:
                            break;
                    }
                });
    }

    private Subscription observeFollowersLikes() {
        return foodBookService.getFollowersLikesRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    UserUpdate followersLike = documentChange.getDocument().toObject(UserUpdate.class);
                    followersLike.setUpdateType(UpdateType.FOLLOWERS_NEW_LIKE);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(followersLike);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeUpdate(followersLike);
                            break;
                        case MODIFIED:
                            homeAdapter.modifyUpdate(followersLike);
                            break;
                        default:
                            break;
                    }
                });
    }

    private Subscription observeMyRecipesComments() {
        return foodBookService.getMyRecipesCommentsRealtime(currentUser.getUid())
                .subscribe(documentChange -> {
                    UserUpdate myRecipeComment = documentChange.getDocument().toObject(UserUpdate.class);
                    myRecipeComment.setUpdateType(UpdateType.MY_RECIPE_NEW_COMMENT);
                    switch (documentChange.getType()) {
                        case ADDED:
                            homeAdapter.addUpdate(myRecipeComment);
                            new Handler().postDelayed(() -> rvHomeRecyclerView.smoothScrollToPosition(0), 1);
                            break;
                        case REMOVED:
                            homeAdapter.removeUpdate(myRecipeComment);
                            break;
                        case MODIFIED:
                            break;
                        default:
                            break;
                    }
                });
    }
}