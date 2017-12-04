package pl.mdanilowski.foodbook.activity.likedRecipes.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;

@Component(modules = LikedRecipesModule.class)
@LikedRecipesScope
public interface LikedRecipesComponent {

    void inject(LikedRecipesActivity activity);
}