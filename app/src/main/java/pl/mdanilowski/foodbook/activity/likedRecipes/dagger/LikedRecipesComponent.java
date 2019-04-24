package pl.mdanilowski.foodbook.activity.likedRecipes.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = LikedRecipesModule.class, dependencies = FoodbookAppComponent.class)
@LikedRecipesScope
public interface LikedRecipesComponent {

    void inject(LikedRecipesActivity activity);
}