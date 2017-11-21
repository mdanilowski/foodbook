package pl.mdanilowski.foodbook.app.dagger.module;


import android.content.Context;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;
import pl.mdanilowski.foodbook.service.FoodBookApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {NetworkModule.class, GsonModule.class, ContextModule.class})
public class ServiceModule {

    @Provides
    @FoodbookAppScope
    public FoodBookApi beerWallService(Retrofit retrofit) {
        return retrofit.create(FoodBookApi.class);
    }

    @Provides
    @FoodbookAppScope
    public Retrofit retrofit(OkHttpClient okHttpClient, Gson gson, Context context) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(context.getString(R.string.api_url))
                .client(okHttpClient)
                .build();
    }
}

