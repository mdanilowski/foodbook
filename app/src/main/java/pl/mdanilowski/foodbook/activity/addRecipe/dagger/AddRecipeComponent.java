package pl.mdanilowski.foodbook.activity.addRecipe.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@AddRecipeScope
@Component(modules = AddRecipeModule.class, dependencies = FoodbookAppComponent.class)
public interface AddRecipeComponent {

    void inject(AddRecipeActivity addRecipeActivity);
}
