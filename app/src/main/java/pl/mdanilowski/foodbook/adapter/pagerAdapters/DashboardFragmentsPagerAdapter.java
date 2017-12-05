package pl.mdanilowski.foodbook.adapter.pagerAdapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pl.mdanilowski.foodbook.activity.dashboard.mvp.DashboardPresenter;
import pl.mdanilowski.foodbook.fragment.dashboard.HomeFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;

public class DashboardFragmentsPagerAdapter extends FragmentStatePagerAdapter {

    private final int FRAGMENT_COUNT = 3;
    private DashboardPresenter context;

    public DashboardFragmentsPagerAdapter(FragmentManager fm, DashboardPresenter context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return RecipesFragment.newInstance(recipe -> context.model.startRecipeDetailsActivity(context.user.getUid(), recipe.getRid()));
            case 2:
                return SearchFragment.newInstance(uid -> context.model.startProfileActivity(uid));
            default:
                return HomeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
