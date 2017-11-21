package pl.mdanilowski.foodbook.app;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.app.dagger.DaggerFoodbookAppComponent;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;
import pl.mdanilowski.foodbook.app.dagger.module.ContextModule;
import timber.log.Timber;

public class App extends Application {

    private static App applicationInstance;
    private FoodbookAppComponent foodbookAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());

        applicationInstance = this;
        foodbookAppComponent = DaggerFoodbookAppComponent.builder().contextModule(new ContextModule(this)).build();

        loadProfileImageIntoDrawerLogic();
    }

    public static App getApplicationInstance() {
        return applicationInstance;
    }

    public FoodbookAppComponent getFoodbookAppComponent() {
        return foodbookAppComponent;
    }

    public void loadProfileImageIntoDrawerLogic() {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(imageView.getContext()).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }
                return super.placeholder(ctx, tag);
            }
        });
    }
}
