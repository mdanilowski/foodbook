package pl.mdanilowski.foodbook.app.dagger.module;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;
import pl.mdanilowski.foodbook.service.FoodBookApi;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

@Module(includes = ServiceModule.class)
public class FirebaseServiceModule {

    @Provides
    @FoodbookAppScope
    public FoodBookService foodBookService(FoodBookApi api){
        return new FoodBookService(api);
    }

    @Provides
    @FoodbookAppScope
    public FoodBookSimpleStorage foodBookSimpleStorage(){
        return new FoodBookSimpleStorage();
    }
}
