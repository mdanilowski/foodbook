package pl.mdanilowski.foodbook.activity.usersRecipes.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.usersRecipes.UsersRecipesActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = UsersRecipesModule.class, dependencies = FoodbookAppComponent.class)
@UsersRecipesScope
public interface UsersRecipesComponent {

    void inject(UsersRecipesActivity usersRecipesActivity);
}
