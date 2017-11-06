package pl.mdanilowski.foodbook.app.dagger;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipePresenter;
import pl.mdanilowski.foodbook.activity.base.BaseActivity;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.SearchResultsAdapter;
import pl.mdanilowski.foodbook.app.dagger.module.FirebaseAuthModule;
import pl.mdanilowski.foodbook.app.dagger.module.FirebaseServiceModule;
import pl.mdanilowski.foodbook.app.dagger.module.GlideModule;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;
import pl.mdanilowski.foodbook.service.FoodBookService;

@FoodbookAppScope
@Component(modules = {FirebaseAuthModule.class, GlideModule.class, FirebaseServiceModule.class})
public interface FoodbookAppComponent {

    FirebaseAuth getFirebaseAuth();

    void inject(BaseActivity baseActivity);

    void inject(BasePresenter basePresenter);

    void inject(RecipesFragment fragment);

    void inject(SearchFragment fragment);

    void inject(FoodBookService foodBookService);

    void inject(AddRecipePresenter addRecipePresenter);

    void inject(SearchResultsAdapter searchResultsAdapter);
}