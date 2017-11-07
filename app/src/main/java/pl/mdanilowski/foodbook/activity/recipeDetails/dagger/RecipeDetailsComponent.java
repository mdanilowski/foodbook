package pl.mdanilowski.foodbook.activity.recipeDetails.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;

@RecipeDetilsScope
@Component(modules = RecipeDetailsModule.class)
public interface RecipeDetailsComponent {

    void inject(RecipeDetailsActivity activity);
}
