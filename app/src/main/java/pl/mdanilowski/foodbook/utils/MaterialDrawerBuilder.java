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

public class MaterialDrawerBuilder {

    public static Drawer setDrawer(BaseActivity activity, Toolbar toolbar, FirebaseUser firebaseUser) {
       return buildDrawerContent(activity, toolbar, firebaseUser);
    }

    private static AccountHeader buildDrawerAccountHeader(BaseActivity activity, FirebaseUser firebaseUser) {
        return new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.color.material_drawer_dark_background)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(firebaseUser.getDisplayName())
                                .withEmail(firebaseUser.getEmail())
                                .withIcon(firebaseUser.getPhotoUrl().toString())
                ).build();
    }

    private static Drawer buildDrawerContent(BaseActivity activity, Toolbar toolbar, FirebaseUser firebaseUser) {
        return new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(buildDrawerAccountHeader(activity, firebaseUser))
                .build();
    }
}
