package pl.mdanilowski.foodbook.app.dagger;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipePresenter;
import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.dagger.module.FirebaseAuthModule;
import pl.mdanilowski.foodbook.app.dagger.module.FirebaseServiceModule;
import pl.mdanilowski.foodbook.app.dagger.module.GlideModule;
import pl.mdanilowski.foodbook.service.FoodBookService;

@FoodbookAppScope
@Component(modules = {FirebaseAuthModule.class, GlideModule.class, FirebaseServiceModule.class})
public interface FoodbookAppComponent {

    FirebaseAuth getFirebaseAuth();

    void inject(BaseActivity baseActivity);

    void inject(BasePresenter basePresenter);

    void inject(Fragment fragment);

    void inject(FoodBookService foodBookService);

    void inject(AddRecipePresenter addRecipePresenter);
}
