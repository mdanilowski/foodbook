package pl.mdanilowski.foodbook.app.dagger.module;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;

@Module
public class GsonModule {

    @Provides
    @FoodbookAppScope
    public Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }
}