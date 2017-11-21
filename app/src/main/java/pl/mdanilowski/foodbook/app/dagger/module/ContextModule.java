package pl.mdanilowski.foodbook.app.dagger.module;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;

@Module
public class ContextModule {

    public final Context context;


    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @FoodbookAppScope
    public Context getContext() {
        return context;
    }
}

