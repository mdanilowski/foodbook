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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private DashboardModel model;
    FirebaseUser user;
    Drawer drawer;

    List<Uri> addedRecipeUris = new ArrayList<>();
    List<Uri> images = new ArrayList<>();
    StorageReference storageReference;
    UploadTask uploadTask;
    Recipe recipeForUpload = null;

    public DashboardPresenter(DashboardModel model, DashboardView view, FirebaseUser user) {
        this.model = model;
        this.view = view;
        this.user = user;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        compositeSubscription.add(observeAvatarClick());
        setViewPagerAndTabs();
        setToolbarAndDrawer();
        if (model.getIsRecipeAddedExtra()) {
            images = model.getRecipeUriListFromIntent();
            recipeForUpload = model.getRecipeFromIntent();
            for (Uri uri : images) {
                uploadImage(uri);
            }
            if (images.isEmpty()) {
                compositeSubscription.add(observeAddingRecipe());
            }
        }
        model.getActivity().getIntent().putExtra(DashboardActivity.IS_RECIPE_ADDED, false);
        compositeSubscription.add(observeFindUser());
        if(Intent.ACTION_SEARCH.equals(model.getActivity().getIntent().getAction())){
            String searchQuery = model.getActivity().getIntent().getStringExtra(SearchManager.QUERY);
            Toast.makeText(view.getContext(), searchQuery, Toast.LENGTH_SHORT).show();
            view.dashboardViewPager.setCurrentItem(3);
        }
    }


    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private void uploadImage(Uri uri) {
        view.showImageUploadingProgress();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        storageReference = storage.getReference().child(user.getUid() + "/" + imageFileName);
        uploadTask = storageReference.putFile(uri);
        uploadTask
                .addOnFailureListener(command -> {
                    addedRecipeUris.add(null);
                    Toast.makeText(view.getContext(), "Failed to upload photo", Toast.LENGTH_SHORT).show();
                    command.printStackTrace();
                })
                .addOnSuccessListener(command -> {
                    addedRecipeUris.add(command.getDownloadUrl());
                    File file = new File(uri.getPath());
                    if (model.getActivity().getContentResolver().delete(uri, null, null) != 0) {
                        Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "NOT deleted", Toast.LENGTH_SHORT).show();
                    }
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
                .subscribe(__ -> view.showSnackBarWithText("Added recipe"),
                        throwable -> {
                            view.showSnackBarWithText("Failed to add recipe");
                            throwable.printStackTrace();
                        });
    }

    private Subscription observeAvatarClick() {
        return view.avatarClick().subscribe(__ -> drawer.openDrawer());
    }

    private Subscription observeFindUser() {
        return foodBookService.findUserByUid(user.getUid()).subscribe(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("USER", "User exists");
            } else {
                compositeSubscription.add(observeAddUser());
            }
        });
    }

    private Subscription observeAddUser() {
        User newUser = new User();
        String[] nameAndSurename = user.getDisplayName().split(" ");
        if (nameAndSurename.length >= 1)
            newUser.setName(nameAndSurename[0]);
        if (nameAndSurename.length >= 2)
            newUser.setSurename(nameAndSurename[1]);
        if (user.getPhotoUrl() != null)
            newUser.setAvatarUrl(user.getPhotoUrl().toString());
        newUser.setEmail(user.getEmail());
        newUser.setFriends(new ArrayList<>());
        newUser.setTotalLikes(0);
        newUser.setRecipesCount(0);
        return foodBookService.setUser(user.getUid(), newUser).subscribe(
                __ -> Log.d("USER", "User added"),
                Throwable::printStackTrace
        );
    }

    private void setToolbarAndDrawer() {
        view.setToolbarProfileImage(user.getPhotoUrl().toString());
        drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar, user);
    }

    private void setViewPagerAndTabs() {
        view.dashboardViewPager.setAdapter(new DashboardFragmentsPagerAdapter(model.getFragmentManager()));
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
