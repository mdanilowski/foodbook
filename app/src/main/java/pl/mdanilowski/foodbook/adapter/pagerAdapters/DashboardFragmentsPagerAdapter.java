package pl.mdanilowski.foodbook.adapter.pagerAdapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pl.mdanilowski.foodbook.fragment.dashboard.HomeFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipeIdeasFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;

public class DashboardFragmentsPagerAdapter extends FragmentStatePagerAdapter {

    private final int FRAGMENT_COUNT = 4;

    public DashboardFragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return RecipesFragment.newInstance();
            case 2:
                return SearchFragment.newInstance();
            case 3:
                return RecipeIdeasFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }
}
