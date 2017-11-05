package pl.mdanilowski.foodbook.app.dagger.module;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

@Module
public class FirebaseServiceModule {

    @Provides
    @FoodbookAppScope
    public FoodBookService foodBookService(){
        return new FoodBookService();
    }

    @Provides
    @FoodbookAppScope
    public FoodBookSimpleStorage foodBookSimpleStorage(){
        return new FoodBookSimpleStorage();
    }
}
