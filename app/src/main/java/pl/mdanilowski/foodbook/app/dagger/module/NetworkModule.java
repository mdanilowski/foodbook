package pl.mdanilowski.foodbook.app.dagger.module;


import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;
import timber.log.Timber;

@Module(includes = ContextModule.class)
public class NetworkModule {

    @Provides
    @FoodbookAppScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @FoodbookAppScope
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                Timber.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @Provides
    @FoodbookAppScope
    public File file(Context context) {
        File file = new File(context.getCacheDir(), "okhttp_cache");
        file.mkdirs();
        return file;
    }

    @Provides
    @FoodbookAppScope
    public Cache cache(File file) {
        return new Cache(file, 10 * 1000 * 1000); //10MB cache
    }
}