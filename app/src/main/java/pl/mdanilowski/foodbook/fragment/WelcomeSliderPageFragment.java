package pl.mdanilowski.foodbook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.mdanilowski.foodbook.R;


public class WelcomeSliderPageFragment extends Fragment {

    public static final String IMG_URL = "IMG_URL";
    public static final String TEXT = "TEXT";
    private int imgId;
    private String pagerText;

    public static WelcomeSliderPageFragment create(int img, String text){
        WelcomeSliderPageFragment welcomeSliderPageFragment = new WelcomeSliderPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMG_URL, img);
        bundle.putString(TEXT, text);
        welcomeSliderPageFragment.setArguments(bundle);
        return welcomeSliderPageFragment;
    }

    public WelcomeSliderPageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgId = this.getArguments().getInt(IMG_URL);
        pagerText = this.getArguments().getString(TEXT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.welcome_icon_with_text, container,false);
        ((ImageView) rootView.findViewById(R.id.ivWelcomePager)).setImageResource(imgId);
        ((TextView) rootView.findViewById(R.id.tvWelcomePagerText)).setText(pagerText);
        return rootView;
    }
}
