package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;

import java.util.List;

import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.model.Recipe;

public class DashboardModel {

    DashboardActivity activity;

    public DashboardModel(DashboardActivity activity) {
        this.activity = activity;
    }

    public DashboardActivity getActivity() {
        return activity;
    }

    public FragmentManager getFragmentManager(){
        return activity.getSupportFragmentManager();
    }

    public void startAddRecipeActivity(){
        AddRecipeActivity.start(activity);
    }

    public Intent getIntent(){
        return activity.getIntent();
    }

    public boolean getIsRecipeAddedExtra(){
        return activity.getIntent().getBooleanExtra(DashboardActivity.IS_RECIPE_ADDED, false);
    }

    public List<Uri> getRecipeUriListFromIntent(){
        return activity.getIntent().getParcelableArrayListExtra(DashboardActivity.IMAGES);
    }

    public boolean getIsNewIntentSearch(){
        return Intent.ACTION_SEARCH.equals(activity.getIntent().getAction());
    }

    public Recipe getRecipeFromIntent(){
        return (Recipe) activity.getIntent().getSerializableExtra(DashboardActivity.RECIPE);
    }

    public void startProfileActivity(String uid){
        ProfileActivity.start(activity, uid);
    }
}

