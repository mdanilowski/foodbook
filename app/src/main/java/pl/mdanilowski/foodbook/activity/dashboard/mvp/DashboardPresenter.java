package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.adapter.pagerAdapters.DashboardFragmentsPagerAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.RecipeQuery;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.Constants;
import pl.mdanilowski.foodbook.utils.MaterialDrawerBuilder;
import rx.Subscription;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DashboardPresenter extends BasePresenter {

    private static final int DRAWER_FIRST_POSITION = 1;
    private static final int DRAWER_SECOND_POSITION = 2;
    private static final int DRAWER_THIRD_POSITION = 3;
    private static final int DRAWER_FOURTH_POSITION = 4;
    private static final int DRAWER_FIFTH_POSITION = 5;
    private static final int DRAWER_SIXT_POSITION = 6;
    private static final int DRAWER_SEVENTH_POSITION = 7;

    private DashboardView view;
    public DashboardModel model;
    public FirebaseUser user;
    private User foodbookUser;
    private Drawer drawer;
    private List<PrimaryDrawerItem> drawerItems;
    private String searchQuery = "";
    private List<Uri> addedRecipeUris = new ArrayList<>();
    private List<Uri> images = new ArrayList<>();
    private Recipe recipeForUpload = null;
    private RecipeQuery recipeQuery = null;

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
        foodbookUser = foodBookSimpleStorage.getUser();
        if (user.getPhotoUrl() == null) {
            UserProfileChangeRequest.Builder creator = new UserProfileChangeRequest.Builder();
            creator.setPhotoUri(Uri.parse(Constants.DEFAULT_AVATAR));
            user.updateProfile(creator.build()).addOnSuccessListener(__ -> {
                setToolbarAndDrawer();
                addItemsToDrawer();
                setDrawerClickListeners();
                compositeSubscription.add(observeAvatarClick());
                compositeSubscription.add(observeFindUser());
            }).addOnFailureListener(e -> {
                drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar);
                addItemsToDrawer();
                setDrawerClickListeners();
                compositeSubscription.add(observeFindUser());
            });
        } else {
            compositeSubscription.add(observeAvatarClick());
            compositeSubscription.add(observeFindUser());
            setToolbarAndDrawer();
            addItemsToDrawer();
            setDrawerClickListeners();
        }
        setViewPagerAndTabs();
        handleIntent();
    }

    private void handleDynamicLink() {
        model.startRecipeDetailsActivity(model.getUidFromIntent(), model.getRidFromIntent());
    }

    public void onNewIntent(Intent intent) {
        model.getActivity().setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        if (model.getIsRecipeAddedExtra()) {
            images = model.getRecipeUriListFromIntent();
            recipeForUpload = model.getRecipeFromIntent();
            recipeQuery = model.getRecipeQueryFromIntent();
            for (Uri uri : images) {
                uploadImage(uri);
            }
            if (images.isEmpty()) {
                compositeSubscription.add(observeAddingRecipe());
            }
            model.getActivity().getIntent().putExtra(DashboardActivity.IS_RECIPE_ADDED, false);
        } else if (model.getIsNewIntentSearch()) {
            searchQuery = model.getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            view.dashboardViewPager.setCurrentItem(2);
        } else if (model.isUserUpdatedIntent()) {
            updateUser();
        } else if (model.isDeepLinkIntent()) {
            handleDynamicLink();
        } else if (foodBookSimpleStorage.getPendingDeepLink() != null) {
            Uri deepLink = foodBookSimpleStorage.getPendingDeepLink();
            String[] path = deepLink.getPath().split("/");
            if (path.length >= 2) {
                String uidFromLink = path[1];
                String ridFromLink = path[2];
                model.startRecipeDetailsActivity(uidFromLink, ridFromLink);
                foodBookSimpleStorage.saveDeepLink(null);
            }
        }
    }

    private void updateUser() {
        if (model.getAvatarUriFromIntent() != null) {
            StorageReference storageReference = storage.getReference().child(user.getUid() + "/avatar");
            UploadTask uploadTask = storageReference.putFile(model.getAvatarUriFromIntent());
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                foodbookUser.setAvatarUrl(taskSnapshot.getUploadSessionUri().toString());
                foodBookSimpleStorage.saveUser(foodbookUser);
                setToolbarAndDrawer();
                addItemsToDrawer();
                setDrawerClickListeners();
                foodBookService.updateUsersAvatarPhoto(foodbookUser.getUid(), taskSnapshot.getUploadSessionUri().toString()).subscribe(aVoid -> {
                    Log.i("UPDATED AVATAR", foodbookUser.getAvatarUrl());
                }, throwable -> {
                    Log.e("ERROR UPLOADING", throwable.getMessage());
                });
            });
        }

        if (model.getBackgroundUriFromIntent() != null) {
            StorageReference storageReference = storage.getReference().child(user.getUid() + "/background");
            UploadTask uploadTask = storageReference.putFile(model.getBackgroundUriFromIntent());
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                foodbookUser.setBackgroundImage(taskSnapshot.getUploadSessionUri().toString());
                foodBookSimpleStorage.saveUser(foodbookUser);
                foodBookService.updateUsersBackgroundPhoto(foodbookUser.getUid(), taskSnapshot.getUploadSessionUri().toString()).subscribe(aVoid -> {
                    Log.i("UPDATED BACK PHOTO", foodbookUser.getAvatarUrl());
                }, throwable -> {
                    Log.e("ERROR UPLOADING", throwable.getMessage());
                });
            });
        }

        if (model.getUpdatedUserFromIntent() != null) {
            foodBookService.setUserSettings(model.getUpdatedUserFromIntent()).subscribe(aVoid -> {
                Log.i("UPDATED USER", "SUCCESS");
            }, throwable -> {
                Log.e("ERROR UPDATING USER", throwable.getMessage());
            });
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
                    Toast.makeText(view.getContext(), R.string.photo_upload_failed, Toast.LENGTH_SHORT).show();
                    command.printStackTrace();
                })
                .addOnSuccessListener(command -> {
                    addedRecipeUris.add(command.getUploadSessionUri());
                    //model.getActivity().getContentResolver().delete(uri, null, null);
                    Toast.makeText(model.getActivity(), R.string.uploaded_image, Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                            if (images.size() == addedRecipeUris.size()) {
                                Iterator<Uri> iterator = addedRecipeUris.iterator();
                                while (iterator.hasNext()) {
                                    if (iterator.next() == null) iterator.remove();
                                }

                                if (addedRecipeUris.size() == 0) {
                                    Snackbar.make(view, R.string.recipe_add_failed, Snackbar.LENGTH_LONG);
                                } else {
                                    for (Uri addedRecipeUri : addedRecipeUris) {
                                        recipeForUpload.getPhotosUrls().add(addedRecipeUri.toString());
                                    }
                                    compositeSubscription.add(observeAddingRecipe());
                                }

                                view.hideImageUploadingProgress();
                            }
                        }
                );
    }

    private Subscription observeAddingRecipe() {
        return foodBookService.addRecipeToUser(user.getUid(), recipeForUpload)
                .subscribe(docRef -> {
                            recipeForUpload.setRid(docRef.getId());
                            recipeQuery.setRid(recipeForUpload.getRid());
                            if (recipeForUpload.getPhotosUrls() != null && recipeForUpload.getPhotosUrls().size() > 0)
                                recipeQuery.setImageUrl(recipeForUpload.getPhotosUrls().get(0));
                            foodBookService.addRecipeToQueryTable(recipeQuery)
                                    .subscribe(aVoid ->
                                            Log.d("ADDED_TO_QUERY", "Added recipe : " + recipeForUpload.getRid() + " to query table"));
                            view.showSnackBarWithText(view.getResources().getString(R.string.added_recipe));
                        },
                        throwable -> {
                            view.showSnackBarWithText(view.getResources().getString(R.string.failed_to_add_recipe));
                            throwable.printStackTrace();
                        });
    }

    private Subscription observeAvatarClick() {
        return view.avatarClick().subscribe(__ -> drawer.openDrawer());
    }

    private Subscription observeFindUser() {
        return foodBookService.getUserRealtime(user.getUid()).subscribe(retrievedUser -> {
            foodBookSimpleStorage.saveUser(retrievedUser);
            foodbookUser = retrievedUser;
            compositeSubscription.add(observeFollowedByUser());
            compositeSubscription.add(observeUsersLikedRecipes());
        }, __ -> {
            compositeSubscription.add(observeAddUser());
        });
    }

    private Subscription observeFollowedByUser() {
        return foodBookService.getUsersFollowedByUserRealtime(user.getUid())
                .subscribe(documentChange -> {
                    switch (documentChange.getType()) {
                        case ADDED:
                            foodbookUser.getFollowing().add(documentChange.getDocument().toObject(User.class));
                            foodBookSimpleStorage.saveUser(foodbookUser);
                            break;
                        case REMOVED:
                            User follower = documentChange.getDocument().toObject(User.class);
                            Iterator<User> iterator = foodbookUser.getFollowing().iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().getUid().equals(follower.getUid()))
                                    iterator.remove();
                            }
                            foodBookSimpleStorage.saveUser(foodbookUser);
                    }
                });
    }

    private Subscription observeUsersLikedRecipes() {
        return foodBookService.getUsersLikedRecipes(user.getUid())
                .subscribe(recipes -> {
                    foodbookUser.getLikedRecipes().clear();
                    for (Recipe r : recipes) {
                        foodbookUser.getLikedRecipes().add(r.getRid());
                    }
                    foodBookSimpleStorage.saveUser(foodbookUser);
                });
    }

    private Subscription observeAddUser() {
        User newUser = new User();
        if (model.isAfterRegister()) {
            newUser = model.getRegisteredUser();
            newUser.setQueryMap(stringToMap(newUser.getName()));
            newUser.setRecipesCount(0);
            newUser.setFollowersCount(0);
            newUser.setFollowingCount(0);
            newUser.setBackgroundImage(null);
            if (newUser.getAvatarUrl() == null) {
                newUser.setAvatarUrl(user.getPhotoUrl().toString());
            }
            if (newUser.getName() == null || TextUtils.isEmpty(newUser.getName())) {
                newUser.setName(user.getDisplayName());
            }
            if (newUser.getAboutMe() == null || TextUtils.isEmpty(newUser.getAboutMe())) {
                newUser.setAboutMe(view.getResources().getString(R.string.about_not_filled));
            }
        } else {
            newUser.setUid(user.getUid());
            newUser.setName(user.getDisplayName());
            newUser.setAvatarUrl(user.getPhotoUrl().toString());
            newUser.setEmail(user.getEmail());
            newUser.setRecipesCount(0);
            newUser.setFollowersCount(0);
            newUser.setFollowingCount(0);
            newUser.setBackgroundImage(null);
            newUser.setAboutMe(view.getResources().getString(R.string.about_not_filled));
            newUser.setCountry("");
            newUser.setCity("");
            newUser.setQueryMap(stringToMap(newUser.getName()));
        }

        User finalNewUser = newUser;
        return foodBookService.setUser(user.getUid(), newUser).subscribe(
                __ -> {
                    Log.d("_USER", "User added");
                    foodBookSimpleStorage.saveUser(finalNewUser);
                    foodbookUser = foodBookSimpleStorage.getUser();
                    compositeSubscription.add(observeFollowedByUser());
                    compositeSubscription.add(observeUsersLikedRecipes());
                },
                Throwable::printStackTrace
        );
    }

    private Map<String, Boolean> stringToMap(String input) {
        Map<String, Boolean> queryMap = new HashMap<>();
        String[] stringTable = input.split(" ");
        for (String s : stringTable) {
            for (int i = 1; i <= s.length(); i++) {
                queryMap.put(s.substring(0, i).toLowerCase(), true);
            }
        }
        return queryMap;
    }

    private void setToolbarAndDrawer() {
        if (foodbookUser == null) {
            view.setToolbarProfileImage(user.getPhotoUrl().toString());
            drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar, user);
        } else {
            view.setToolbarProfileImage(foodbookUser.getAvatarUrl());
            drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar, foodbookUser);
        }
    }

    private void addItemsToDrawer() {

        drawerItems = new ArrayList<>();

        PrimaryDrawerItem profile = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName(R.string.your_profile);
        PrimaryDrawerItem accountSettings = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.ustawienia_konta);
        PrimaryDrawerItem youFollow = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.people_you_follow);
        PrimaryDrawerItem followers = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.followers);
        PrimaryDrawerItem likedRecipes = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName(R.string.liked_recipes);
        PrimaryDrawerItem findFriends = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName(R.string.find_friends);
        PrimaryDrawerItem logout = new PrimaryDrawerItem()
                .withIdentifier(6)
                .withName(R.string.logout);

        drawer.setItemAtPosition(profile, DRAWER_FIRST_POSITION);
        drawer.setItemAtPosition(accountSettings, DRAWER_SECOND_POSITION);
        drawer.setItemAtPosition(youFollow, DRAWER_THIRD_POSITION);
        drawer.setItemAtPosition(followers, DRAWER_FOURTH_POSITION);
        drawer.setItemAtPosition(likedRecipes, DRAWER_FIFTH_POSITION);
        drawer.setItemAtPosition(findFriends, DRAWER_SIXT_POSITION);
        drawer.setItemAtPosition(logout, DRAWER_SEVENTH_POSITION);
    }

    private void setDrawerClickListeners() {
        drawer.setOnDrawerItemClickListener((view1, position, drawerItem) -> {
            switch (position) {
                case DRAWER_FIRST_POSITION:
                    drawer.closeDrawer();
                    model.startProfileActivity(foodBookSimpleStorage.getUser().getUid());
                    return true;
                case DRAWER_SECOND_POSITION:
                    drawer.closeDrawer();
                    model.startUserSettingsActivity(foodbookUser);
                    return true;
                case DRAWER_THIRD_POSITION:
                    drawer.closeDrawer();
                    model.startFollowingActivity();
                    return true;
                case DRAWER_FOURTH_POSITION:
                    drawer.closeDrawer();
                    model.startFollowersActivity(foodbookUser.getUid());
                    return true;
                case DRAWER_FIFTH_POSITION:
                    drawer.closeDrawer();
                    model.startLikedRecipesActivity();
                    return true;
                case DRAWER_SIXT_POSITION:
                    drawer.closeDrawer();
                    model.startFindFriendsActibity();
                    return true;
                case DRAWER_SEVENTH_POSITION:
                    drawer.closeDrawer();
                    compositeSubscription.clear();
                    firebaseAuth.signOut();
                    model.startWelcomeActivity();
                    return true;
                default:
                    drawer.closeDrawer();
                    return true;
            }
        });
    }

    private void setViewPagerAndTabs() {
        view.dashboardViewPager.setAdapter(new DashboardFragmentsPagerAdapter(model.getFragmentManager(), this));
        view.dashboardTabs.setupWithViewPager(view.dashboardViewPager);
        view.dashboardViewPager.setOffscreenPageLimit(3);

        view.dashboardViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        view.collapseSearchView();
                        view.dashboardTabs.getTabAt(0).getIcon().setColorFilter(view.getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(1).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(2).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 1:
                        view.collapseSearchView();
                        view.dashboardTabs.getTabAt(0).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(1).getIcon().setColorFilter(view.getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(2).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 2:
                        view.expandSearchView();
                        view.dashboardTabs.getTabAt(0).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(1).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
                        view.dashboardTabs.getTabAt(2).getIcon().setColorFilter(view.getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTabIcons();
    }

    private void setTabIcons() {
        view.dashboardTabs.getTabAt(0).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(1).setIcon(R.drawable.ic_my_recipes);
        view.dashboardTabs.getTabAt(2).setIcon(R.mipmap.ic_search_results);

        view.dashboardTabs.getTabAt(0).getIcon().setColorFilter(view.getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        view.dashboardTabs.getTabAt(1).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
        view.dashboardTabs.getTabAt(2).getIcon().setColorFilter(view.getResources().getColor(R.color.white_100), PorterDuff.Mode.SRC_ATOP);
    }

    public void gotoSearchTab() {
        view.dashboardViewPager.setCurrentItem(2, true);
    }

    public void setSearchListeners() {
        view.setOnSearchListener(view1 -> {
            view.toolbarTitle.setVisibility(GONE);
            view.ivAvatar.setVisibility(GONE);
            gotoSearchTab();
        });
        view.setOnSearchCloseListener(() -> {
            view.toolbarTitle.setVisibility(VISIBLE);
            view.ivAvatar.setVisibility(VISIBLE);
            view.searchView.onActionViewCollapsed();
            return true;
        });
    }
}
