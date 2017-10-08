package pl.mdanilowski.foodbook.activity.splash.mvp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import pl.mdanilowski.foodbook.R;

public class SplashView extends FrameLayout {

    public SplashView(@NonNull Context context) {
        super(context);

        inflate(context, R.layout.activity_splash, this);
    }
}
