package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.adapter.pagerAdapters.DashboardFragmentsPagerAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.MaterialDrawerBuilder;
import rx.Subscription;

public class DashboardPresenter extends BasePresenter {

    private DashboardView view;
    public DashboardModel model;
    private FirebaseUser user;
    private Drawer drawer;
    private String searchQuery = "";

    private List<Uri> addedRecipeUris = new ArrayList<>();
    private List<Uri> images = new ArrayList<>();
    private Recipe recipeForUpload = null;

    public String getSearchQuery() {
        return searchQuery;
    }

    public DashboardPresenter(DashboardModel model, DashboardView view, FirebaseUser user) {
        this.model = model;
        this.view = view;
        this.user = user;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        compositeSubscription.add(observeAvatarClick());
        compositeSubscription.add(observeFindUser());
        setViewPagerAndTabs();
        setToolbarAndDrawer();
        handleIntent();
    }

    public void onNewIntent(Intent intent) {
        model.getActivity().setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        if (model.getIsRecipeAddedExtra()) {
            images = model.getRecipeUriListFromIntent();
            recipeForUpload = model.getRecipeFromIntent();
            for (Uri uri : images) {
                uploadImage(uri);
            }
            if (images.isEmpty()) {
                compositeSubscription.add(observeAddingRecipe());
            }
            model.getActivity().getIntent().putExtra(DashboardActivity.IS_RECIPE_ADDED, false);
        }
        if (model.getIsNewIntentSearch()) {
            searchQuery = model.getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            view.dashboardViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private void uploadImage(Uri uri) {
        view.showImageUploadingProgress();
        StorageReference storageReference = storage.getReference().child(user.getUid() + "/" + uri.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(uri);
        uploadTask
                .addOnFailureListener(command -> {
                    addedRecipeUris.add(null);
                    Toast.makeText(view.getContext(), "Failed to upload photo", Toast.LENGTH_SHORT).show();
                    command.printStackTrace();
                })
                .addOnSuccessListener(command -> {
                    addedRecipeUris.add(command.getDownloadUrl());
                    model.getActivity().getContentResolver().delete(uri, null, null);
                    Toast.makeText(model.getActivity(), "Uploaded image", Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                            if (images.size() == addedRecipeUris.size()) {
                                Iterator<Uri> iterator = addedRecipeUris.iterator();
                                while (iterator.hasNext()) {
                                    if (iterator.next() == null) iterator.remove();
                                }

                                for (Uri addedRecipeUri : addedRecipeUris) {
                                    recipeForUpload.getPhotosUrls().add(addedRecipeUri.toString());
                                }
                                view.hideImageUploadingProgress();
                                compositeSubscription.add(observeAddingRecipe());
                            }
                        }
                );
    }

    private Subscription observeAddingRecipe() {
        return foodBookService.addRecipeToUser(user.getUid(), recipeForUpload)
                .subscribe(__ -> view.showSnackBarWithText(view.getResources().getString(R.string.added_recipe)),
                        throwable -> {
                            view.showSnackBarWithText(view.getResources().getString(R.string.failed_to_add_recipe));
                            throwable.printStackTrace();
                        });
    }

    private Subscription observeAvatarClick() {
        return view.avatarClick().subscribe(__ -> drawer.openDrawer());
    }

    private Subscription observeFindUser() {
        return foodBookService.findUserByUid(user.getUid()).subscribe(retrievedUser -> {
            foodBookSimpleStorage.saveUser(retrievedUser);
            compositeSubscription.add(observeFindUsersFriends());
        }, __ -> compositeSubscription.add(observeAddUser()));
    }

    private Subscription observeFindUsersFriends() {
        return foodBookService.getUsersFollowedByUser(user.getUid())
                .subscribe(documentChange -> {
                    User user = foodBookSimpleStorage.getUser();
                    switch (documentChange.getType()) {
                        case ADDED:
                            user.getFollowing().add(documentChange.getDocument().toObject(User.class));
                            foodBookSimpleStorage.saveUser(user);
                            break;
                        case REMOVED:
                            User follower = documentChange.getDocument().toObject(User.class);
                            Iterator<User> iterator = user.getFollowing().iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().getUid().equals(follower.getUid()))
                                    iterator.remove();
                            }
                            foodBookSimpleStorage.saveUser(user);
                    }
                });
    }

    private Subscription observeAddUser() {
        User newUser = new User();
        newUser.setUid(user.getUid());
        newUser.setName(user.getDisplayName());
        newUser.setAvatarUrl(user.getPhotoUrl().toString());
        newUser.setEmail(user.getEmail());
        newUser.setFollowers(new ArrayList<>());
        newUser.setFollowing(new ArrayList<>());
        newUser.setTotalLikes(0);
        newUser.setRecipesCount(0);
        newUser.setFollowersCount(0);
        newUser.setFollowingCount(0);
        newUser.setBackgroundImage(null);
        newUser.setAboutMe("Hello, I haven't filled this yet... be patient ;)");
        newUser.setCountry("");
        newUser.setCity("");
        return foodBookService.setUser(user.getUid(), newUser).subscribe(
                __ -> {
                    Log.d("_USER", "User added");
                    compositeSubscription.add(observeFindUsersFriends());
                },
                Throwable::printStackTrace
        );
    }

    private void setToolbarAndDrawer() {
        view.setToolbarProfileImage(user.getPhotoUrl().toString());
        drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar, user);
    }

    private void setViewPagerAndTabs() {
        view.dashboardViewPager.setAdapter(new DashboardFragmentsPagerAdapter(model.getFragmentManager(), this));
        view.dashboardTabs.setupWithViewPager(view.dashboardViewPager);
        view.dashboardViewPager.setOffscreenPageLimit(4);
        setTabIcons();
    }

    private void setTabIcons() {
        view.dashboardTabs.getTabAt(0).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(1).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(2).setIcon(R.mipmap.search_icon);
        view.dashboardTabs.getTabAt(3).setIcon(R.mipmap.search_icon);
    }

    public void onFragmentInteraction(Uri uri) {

    }
}
