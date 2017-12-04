package pl.mdanilowski.foodbook.app.dagger.module;


import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.AppGlideModule;

import dagger.Module;

@com.bumptech.glide.annotation.GlideModule
@Module
public class GlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.VERBOSE);
    }
}
