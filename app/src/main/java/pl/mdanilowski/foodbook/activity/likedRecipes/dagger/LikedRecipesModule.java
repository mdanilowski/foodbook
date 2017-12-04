package pl.mdanilowski.foodbook.activity.likedRecipes.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;
import pl.mdanilowski.foodbook.activity.likedRecipes.mvp.LikedRecipesModel;
import pl.mdanilowski.foodbook.activity.likedRecipes.mvp.LikedRecipesPresenter;
import pl.mdanilowski.foodbook.activity.likedRecipes.mvp.LikedRecipesView;

@Module
public class LikedRecipesModule {

    private LikedRecipesActivity activity;

    public LikedRecipesModule(LikedRecipesActivity activity) {
        this.activity = activity;
    }

    @Provides
    @LikedRecipesScope
    public LikedRecipesModel model(){
        return new LikedRecipesModel(activity);
    }

    @Provides
    @LikedRecipesScope
    public LikedRecipesView view(){
        return new LikedRecipesView(activity);
    }

    @Provides
    @LikedRecipesScope
    public LikedRecipesPresenter presenter(LikedRecipesView view, LikedRecipesModel model){
        return new LikedRecipesPresenter(view, model);
    }
}
