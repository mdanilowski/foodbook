package pl.mdanilowski.foodbook.app.dagger;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.dagger.module.FirebaseAuthModule;
import pl.mdanilowski.foodbook.app.dagger.module.GlideModule;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.service.FoodBookService;

@FoodbookAppScope
@Component(modules = {FirebaseAuthModule.class, GlideModule.class})
public interface FoodbookAppComponent {

    FirebaseAuth getFirebaseAuth();

    void inject(BaseActivity baseActivity);

    void inject(BasePresenter basePresenter);

    void inject(RecipesFragment recipesFragment);

    void inject(FoodBookService foodBookService);
}
