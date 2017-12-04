package pl.mdanilowski.foodbook.activity.addRecipe.dagger;


import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipeModel;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipePresenter;
import pl.mdanilowski.foodbook.activity.addRecipe.mvp.AddRecipeView;

@Module
public class AddRecipeModule {

    AddRecipeActivity activity;

    public AddRecipeModule(AddRecipeActivity activity) {
        this.activity = activity;
    }

    @Provides
    @AddRecipeScope
    public AddRecipeActivity activity(){
        return activity;
    }

    @Provides
    @AddRecipeScope
    public AddRecipeModel model(AddRecipeActivity activity){
        return new AddRecipeModel(activity);
    }

    @Provides
    @AddRecipeScope
    public AddRecipeView view(AddRecipeActivity activity){
        return new AddRecipeView(activity);
    }

    @Provides
    @AddRecipeScope
    public AddRecipePresenter presenter(AddRecipeView view, AddRecipeModel model){
        return new AddRecipePresenter(view, model);
    }
}
