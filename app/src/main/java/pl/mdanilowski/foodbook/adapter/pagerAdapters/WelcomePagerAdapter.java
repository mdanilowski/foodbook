package pl.mdanilowski.foodbook.adapter.pagerAdapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.fragment.WelcomeSliderPageFragment;

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {

    List<Integer> imgs = new ArrayList<>();
    List<String> strings = new ArrayList<>();

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);
        for(int i=1; i<4; i++){
            imgs.add(R.drawable.foodiconplaceholder);
        }
        strings.add("Pierwsza strona view pagera.");
        strings.add("Druga strona view pagera.");
        strings.add("Trzecia strona view pagera.");
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomeSliderPageFragment.create(imgs.get(position), strings.get(position));
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

}
