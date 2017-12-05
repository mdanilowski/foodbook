package pl.mdanilowski.foodbook.activity.splash.mvp;


import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;

public class SplashPresenter extends BasePresenter {

    private final SplashView view;
    private final SplashModel model;
    FirebaseAuth firebaseAuth;

    public SplashPresenter(SplashView view, SplashModel model, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.model = model;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onCreate() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            model.startWelcomeActivity();
        } else {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(model.getIntent())
                    .addOnSuccessListener(runnable -> {
                        Uri deepLink;
                        if (runnable != null) {
                            deepLink = runnable.getLink();
                            String[] path = deepLink.getPath().split("/");
                            if (path.length >= 2) {
                                String uidFromLink = path[1];
                                String ridFromLink = path[2];
                                model.startDashboardWithDeepLink(uidFromLink, ridFromLink);
                            }
                        } else {
                            model.startDashboardActivity();
                        }
                    })
                    .addOnFailureListener(exception -> {
                                model.startDashboardActivity();
                                Log.e("DEEP_LINK_ERROR", exception.getMessage());
                            }
                    );
        }
    }

    @Override
    public void onDestroy() {

    }
}
