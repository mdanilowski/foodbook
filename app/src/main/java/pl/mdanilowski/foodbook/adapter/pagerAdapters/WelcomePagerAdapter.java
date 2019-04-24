package pl.mdanilowski.foodbook.adapter.pagerAdapters;


import android.content.Context;
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

    public WelcomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        imgs.add(R.drawable.ic_zapisuj_przepisy);
        imgs.add(R.drawable.ic_szukaj_inspiracji);
        imgs.add(R.drawable.ic_dziel_sie_pomyslami);
        strings.add(context.getResources().getString(R.string.save_recipes));
        strings.add(context.getString(R.string.look_for_ispirations));
        strings.add(context.getString(R.string.share_recipes));
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
