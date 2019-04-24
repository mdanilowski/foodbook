package pl.mdanilowski.foodbook.activity.editRecipe.mvp;


import android.app.ProgressDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import rx.Subscription;

public class EditRecipePresenter extends BasePresenter {

    private static final String RECIPE_NAME_KEY = "name";
    private static final String RECIPE_DESCRIPTION_KEY = "description";
    private static final String RECIPE_INGREDIENTS_KEY = "ingredients";
    private static final String RECIPE_TAGS_KEY = "tags";

    private EditRecipeView view;
    private EditRecipeModel model;
    private Recipe receivedRecipe;

    private ProgressDialog loadingDialog;

    public EditRecipePresenter(EditRecipeView view, EditRecipeModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);

        receivedRecipe = model.getRecipeFromIntent();
        if (receivedRecipe != null) {
            view.setRecipeData(receivedRecipe);
        }
        compositeSubscription.add(observeSaveRecipeClick());
        compositeSubscription.add(observeAddIngredientClick());
        compositeSubscription.add(observeAddTagClick());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeSaveRecipeClick() {
        return view.saveBtnClick().subscribe(__ -> {
            loadingDialog = new ProgressDialog(view.getContext());
            loadingDialog.setMessage(view.getResources().getString(R.string.updating_recipe));
            loadingDialog.show();
            updateRecipe();
        });
    }

    private Subscription observeAddIngredientClick() {
        return view.addIngredientClick().subscribe(__ -> view.addIngredientEditText());
    }

    private Subscription observeAddTagClick() {
        return view.addTagClick().subscribe(__ -> view.addTagEditText());
    }

    private void updateRecipe() {
        String newName = String.valueOf(view.etName.getText());
        String newDescription = String.valueOf(view.etDescription.getText());
        List<String> newIngredients = view.prepareIngredients();
        List<String> newTags = view.prepareTags();

        Map<String, Object> updateMap = new HashMap<>();
        if (!newName.equals(receivedRecipe.getName())) {
            updateMap.put(RECIPE_NAME_KEY, newName);
        }

        if (!newDescription.equals(receivedRecipe.getDescription())) {
            updateMap.put(RECIPE_DESCRIPTION_KEY, newDescription);
        }

        if (newIngredients.size() != receivedRecipe.getIngredients().size()) {
            updateMap.put(RECIPE_INGREDIENTS_KEY, newIngredients);
        } else if (!isStringListEqual(newIngredients, receivedRecipe.getIngredients())) {
            updateMap.put(RECIPE_INGREDIENTS_KEY, newIngredients);
        }

        if (newTags.size() != receivedRecipe.getTags().size()) {
            updateMap.put(RECIPE_TAGS_KEY, newTags);
        } else if (!isStringListEqual(newTags, receivedRecipe.getTags())) {
            updateMap.put(RECIPE_TAGS_KEY, newTags);
        }

        foodBookService.updateRecipe(receivedRecipe.getOid(), receivedRecipe.getRid(), updateMap)
                .subscribe(__ -> {
                    loadingDialog.hide();
                    model.back();
                });
    }

    private boolean isStringListEqual(List<String> list, List<String> compareList) {
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equals(compareList.get(i))) return false;
        }
        return true;
    }
}
