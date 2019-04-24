package pl.mdanilowski.foodbook.activity.editRecipe.mvp;


import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.editRecipe.EditRecipeActivity;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.utils.SwipeDismissTouchListener;
import rx.Observable;

public class EditRecipeView extends FrameLayout {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.llIngredients) LinearLayout llIngredients;
    @BindView(R.id.btnAddIngredient) CircleImageView btnAddIngredient;
    @BindView(R.id.etIngredient) TextInputEditText etIngredient;
    @BindView(R.id.etTag) TextInputEditText etTag;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.llTags) LinearLayout llTags;
    @BindView(R.id.btnAddTag) CircleImageView btnAddTag;
    @BindView(R.id.btnSaveRecipe) Button btnSaveRecipe;

    LayoutInflater inflater;

    public EditRecipeView(@NonNull EditRecipeActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_edit_recipe, this);
        ButterKnife.bind(this);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        tvToolbarTitle.setText(getResources().getString(R.string.edit));
    }

    public void setRecipeData(Recipe recipeData) {
        etName.setText(recipeData.getName());
        etDescription.setText(recipeData.getDescription());

        List<String> ingerdients = recipeData.getIngredients();
        if (ingerdients.get(0) != null) {
            etIngredient.setText(ingerdients.get(0));
        }
        for (int i = 1; i < ingerdients.size(); i++) {
            TextInputEditText view = (TextInputEditText) inflater.inflate(R.layout.view_recipe_edit_text, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            params.setMargins(0, 0, 0, 16);
            view.setLayoutParams(params);
            SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(view, null, new SwipeDismissTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(Object token) {
                    return true;
                }

                @Override
                public void onDismiss(View view, Object token) {
                    llIngredients.removeView(view);
                }
            });
            view.setOnTouchListener(swipeDismissTouchListener);
            view.setText(ingerdients.get(i));
            llIngredients.addView(view);
        }

        List<String> tags = recipeData.getTags();
        if (tags.get(0) != null) {
            etTag.setText(tags.get(0));
        }
        for (int i = 1; i < tags.size(); i++) {
            TextInputEditText view = (TextInputEditText) inflater.inflate(R.layout.view_recipe_edit_text, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            params.setMargins(0, 0, 0, 16);
            view.setLayoutParams(params);
            SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(view, null, new SwipeDismissTouchListener.DismissCallbacks() {
                @Override
                public boolean canDismiss(Object token) {
                    return true;
                }

                @Override
                public void onDismiss(View view, Object token) {
                    llIngredients.removeView(view);
                }
            });
            view.setOnTouchListener(swipeDismissTouchListener);
            view.setText(tags.get(i));
            llTags.addView(view);
        }
    }

    public ArrayList<String> prepareTags() {
        ArrayList<String> tags = new ArrayList<>();
        EditText et;
        tags.add(String.valueOf(etTag.getText()));
        for (int i = 1; i < llTags.getChildCount(); i++) {
            et = (TextInputEditText) llTags.getChildAt(i);
            tags.add(String.valueOf(et.getText()));
        }
        return tags;
    }

    public List<String> prepareIngredients() {
        List<String> ingredients = new ArrayList<>();
        EditText et;
        ingredients.add(String.valueOf(etIngredient.getText()));
        for (int i = 1; i < llIngredients.getChildCount(); i++) {
            et = (TextInputEditText) llIngredients.getChildAt(i);
            ingredients.add(String.valueOf(et.getText()));
        }
        return ingredients;
    }

    void addTagEditText() {
        EditText view = (EditText) inflater.inflate(R.layout.view_recipe_edit_text, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(0, 0, 0, 16);
        view.setLayoutParams(params);
        view.setHint(R.string.enter_tag);
        SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(view, null, new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(Object token) {
                return true;
            }

            @Override
            public void onDismiss(View view, Object token) {
                llTags.removeView(view);
            }
        });
        view.setOnTouchListener(swipeDismissTouchListener);
        llTags.addView(view);
    }

    void addIngredientEditText() {
        TextInputEditText view = (TextInputEditText) inflater.inflate(R.layout.view_recipe_edit_text, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(0, 0, 0, 16);
        view.setLayoutParams(params);
        view.setHint(R.string.enter_ingredient);
        SwipeDismissTouchListener swipeDismissTouchListener = new SwipeDismissTouchListener(view, null, new SwipeDismissTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(Object token) {
                return true;
            }

            @Override
            public void onDismiss(View view, Object token) {
                llIngredients.removeView(view);
            }
        });
        view.setOnTouchListener(swipeDismissTouchListener);
        llIngredients.addView(view);
    }

    Observable<Void> addIngredientClick() {
        return RxView.clicks(btnAddIngredient);
    }

    Observable<Void> addTagClick() {
        return RxView.clicks(btnAddTag);
    }

    Observable<Void> saveBtnClick() {
        return RxView.clicks(btnSaveRecipe);
    }
}
