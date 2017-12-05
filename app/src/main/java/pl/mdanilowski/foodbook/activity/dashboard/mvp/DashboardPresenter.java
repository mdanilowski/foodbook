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
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.MaterialDrawerBuilder;
import rx.Subscription;

public class DashboardPresenter extends BasePresenter {

    private static final int DRAWER_FIRST_POSITION = 1;
    private static final int DRAWER_SECOND_POSITION = 2;
    private static final int DRAWER_THIRD_POSITION = 3;
    private static final int DRAWER_FOURTH_POSITION = 4;
    private static final int DRAWER_FIFTH_POSITION = 5;

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
        compositeSubscription.add(observeAvatarClick());
        compositeSubscription.add(observeFindUser());
        setViewPagerAndTabs();
        setToolbarAndDrawer();
        addItemsToDrawer();
        setDrawerClickListeners();
        handleIntent();
    }

    private void handleDynamicLink() {
//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(model.getIntent())
//                .addOnSuccessListener(runnable -> {
//                    Uri deepLink;
//                    if (runnable != null) {
//                        deepLink = runnable.getLink();
//                        String[] path = deepLink.getPath().split("/");
//                        if (path.length >= 2) {
//                            String uidFromLink = path[1];
//                            String ridFromLink = path[2];
//                            model.startRecipeDetailsActivity(uidFromLink, ridFromLink);
//                        }
//                    }
//                })
//                .addOnFailureListener(exception -> Log.e("DEEP_LINK_ERROR", exception.getMessage()));
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
        } else if(model.isDeepLinkIntent()) {
            handleDynamicLink();
        }
    }

    private void updateUser() {
        if (model.getAvatarUriFromIntent() != null) {
            StorageReference storageReference = storage.getReference().child(user.getUid() + "/avatar");
            UploadTask uploadTask = storageReference.putFile(model.getAvatarUriFromIntent());
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                foodbookUser.setAvatarUrl(taskSnapshot.getDownloadUrl().toString());
                foodBookSimpleStorage.saveUser(foodbookUser);
                setToolbarAndDrawer();
                addItemsToDrawer();
                setDrawerClickListeners();
                foodBookService.updateUsersAvatarPhoto(foodbookUser.getUid(), taskSnapshot.getDownloadUrl().toString()).subscribe(aVoid -> {
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
                foodbookUser.setBackgroundImage(taskSnapshot.getDownloadUrl().toString());
                foodBookSimpleStorage.saveUser(foodbookUser);
                foodBookService.updateUsersBackgroundPhoto(foodbookUser.getUid(), taskSnapshot.getDownloadUrl().toString()).subscribe(aVoid -> {
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
                .subscribe(docRef -> {
                            recipeForUpload.setRid(docRef.getId());
                            foodBookService.addRecipeToQueryTable(recipeForUpload)
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
        return foodBookService.findUserByUid(user.getUid()).subscribe(retrievedUser -> {
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
        newUser.setUid(user.getUid());
        newUser.setName(user.getDisplayName());
        newUser.setAvatarUrl(user.getPhotoUrl().toString());
        newUser.setEmail(user.getEmail());
        newUser.setRecipesCount(0);
        newUser.setFollowersCount(0);
        newUser.setFollowingCount(0);
        newUser.setBackgroundImage(null);
        newUser.setAboutMe("Hello, I haven't filled this yet... be patient ;)");
        newUser.setCountry("");
        newUser.setCity("");
        newUser.setQueryMap(stringToMap(newUser.getName()));
        return foodBookService.setUser(user.getUid(), newUser).subscribe(
                __ -> {
                    Log.d("_USER", "User added");
                    foodBookSimpleStorage.saveUser(newUser);
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

        PrimaryDrawerItem accountSettings = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName("Account Settings");
        PrimaryDrawerItem youFollow = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("People you follow");
        PrimaryDrawerItem followers = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Followers");
        PrimaryDrawerItem likedRecipes = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Liked recipes");
        PrimaryDrawerItem findFriends = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Find friends");

        drawer.setItemAtPosition(accountSettings, DRAWER_FIRST_POSITION);
        drawer.setItemAtPosition(youFollow, DRAWER_SECOND_POSITION);
        drawer.setItemAtPosition(followers, DRAWER_THIRD_POSITION);
        drawer.setItemAtPosition(likedRecipes, DRAWER_FOURTH_POSITION);
        drawer.setItemAtPosition(findFriends, DRAWER_FIFTH_POSITION);
    }

    private void setDrawerClickListeners() {
        drawer.setOnDrawerItemClickListener((view1, position, drawerItem) -> {
            switch (position) {
                case DRAWER_FIRST_POSITION:
                    drawer.closeDrawer();
                    model.startUserSettingsActivity(foodbookUser);
                    return true;
                case DRAWER_SECOND_POSITION:
                    drawer.closeDrawer();
                    model.startFollowingActivity();
                    return true;
                case DRAWER_THIRD_POSITION:
                    drawer.closeDrawer();
                    model.startFollowersActivity(foodbookUser.getUid());
                    return true;
                case DRAWER_FOURTH_POSITION:
                    drawer.closeDrawer();
                    model.startLikedRecipesActivity();
                    return true;
                case DRAWER_FIFTH_POSITION:
                    drawer.closeDrawer();
                    model.startFindFriendsActibity();
                    return true;
                default:
                    drawer.closeDrawer();
                    Toast.makeText(view.getContext(), "DEFAULT", Toast.LENGTH_SHORT).show();
                    return true;
            }
        });
    }

    private void setViewPagerAndTabs() {
        view.dashboardViewPager.setAdapter(new DashboardFragmentsPagerAdapter(model.getFragmentManager(), this));
        view.dashboardTabs.setupWithViewPager(view.dashboardViewPager);
        view.dashboardViewPager.setOffscreenPageLimit(3);
        setTabIcons();
    }

    private void setTabIcons() {
        view.dashboardTabs.getTabAt(0).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(1).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(2).setIcon(R.mipmap.search_icon);
//        view.dashboardTabs.getTabAt(3).setIcon(R.mipmap.search_icon);
    }
}
