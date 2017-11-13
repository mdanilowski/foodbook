package pl.mdanilowski.foodbook.activity.usersRecipes.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.usersRecipes.UsersRecipesActivity;
import pl.mdanilowski.foodbook.activity.usersRecipes.mvp.UsersRecipesModel;
import pl.mdanilowski.foodbook.activity.usersRecipes.mvp.UsersRecipesPresenter;
import pl.mdanilowski.foodbook.activity.usersRecipes.mvp.UsersRecipesView;

@Module
public class UsersRecipesModule {

    private UsersRecipesActivity activity;

    public UsersRecipesModule(UsersRecipesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @UsersRecipesScope
    public UsersRecipesActivity getActivity(){
        return activity;
    }

    @Provides
    @UsersRecipesScope
    public UsersRecipesModel model(){
        return new UsersRecipesModel(activity);
    }


    @Provides
    @UsersRecipesScope
    public UsersRecipesView view(){
        return new UsersRecipesView(activity);
    }


    @Provides
    @UsersRecipesScope
    public UsersRecipesPresenter presenter(UsersRecipesView view, UsersRecipesModel model){
        return new UsersRecipesPresenter(view, model);
    }

}
