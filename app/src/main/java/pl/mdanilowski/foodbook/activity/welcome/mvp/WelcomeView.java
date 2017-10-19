package pl.mdanilowski.foodbook.activity.welcome.mvp;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.adapter.pagerAdapters.WelcomePagerAdapter;
import rx.Observable;

public class WelcomeView extends FrameLayout {

    @BindView(R.id.ivWelcomeBackground)
    ImageView ivWelcomeBackground;

    @BindView(R.id.welcomePager)
    ViewPager welcomePager;

    @BindView(R.id.hsvWelcome)
    HorizontalScrollView hsvWelcome;

    @BindView(R.id.btnLoginRequest)
    Button btnLoginRequest;

    public WelcomeView(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        inflate(context, R.layout.activity_welcome, this);
        ButterKnife.bind(this);

        welcomePager.setAdapter(new WelcomePagerAdapter(fragmentManager));
        welcomePager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            welcomePager.setOnScrollChangeListener(new OnScrollListener());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private class OnScrollListener implements OnScrollChangeListener{

        @Override
        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
               hsvWelcome.setSmoothScrollingEnabled(true);
               hsvWelcome.smoothScrollTo(i2/4,i3/4);
        }
    }

    private class ZoomOutPageTransformer implements ViewPager.PageTransformer{

        private static final float MIN_SCALE = 0.7f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    Observable<Void> loginButtonClick(){
        return RxView.clicks(btnLoginRequest);
    }
}
