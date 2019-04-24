package pl.mdanilowski.foodbook.activity.editRecipe.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.editRecipe.EditRecipeActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@EditRecipeScope
@Component(modules = EditRecipeModule.class, dependencies = FoodbookAppComponent.class)
public interface EditRecipeComponent {

    void inject(EditRecipeActivity activity);
}
