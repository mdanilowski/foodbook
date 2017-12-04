package pl.mdanilowski.foodbook.utils;


import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.model.User;

public class MaterialDrawerBuilder {

    public static Drawer setDrawer(BaseActivity activity, Toolbar toolbar, FirebaseUser firebaseUser) {
       return buildDrawerContent(activity, toolbar, firebaseUser);
    }

    private static Drawer buildDrawerContent(BaseActivity activity, Toolbar toolbar, FirebaseUser firebaseUser) {
        return new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(buildDrawerAccountHeader(activity, firebaseUser))
                .build();
    }

    private static AccountHeader buildDrawerAccountHeader(BaseActivity activity, FirebaseUser firebaseUser) {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.primary_dark)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(firebaseUser.getDisplayName())
                                .withEmail(firebaseUser.getEmail())
                                .withIcon(firebaseUser.getPhotoUrl().toString())
                ).build();
    }


    public static Drawer setDrawer(BaseActivity activity, Toolbar toolbar, User foodbookUser) {
        return buildDrawerContent(activity, toolbar, foodbookUser);
    }

    private static Drawer buildDrawerContent(BaseActivity activity, Toolbar toolbar, User foodbookUser) {
        return new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(buildDrawerAccountHeader(activity, foodbookUser))
                .build();
    }

    private static AccountHeader buildDrawerAccountHeader(BaseActivity activity, User foodbookUser) {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(foodbookUser.getName())
                                .withEmail(foodbookUser.getEmail())
                                .withIcon(foodbookUser.getAvatarUrl())
                ).build();
    }
}
