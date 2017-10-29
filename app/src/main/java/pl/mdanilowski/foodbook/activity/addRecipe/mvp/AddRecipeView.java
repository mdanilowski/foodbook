package pl.mdanilowski.foodbook.activity.addRecipe.mvp;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.AddRecipePhotosAdapter;
import pl.mdanilowski.foodbook.model.Ingredient;
import pl.mdanilowski.foodbook.utils.SwipeDismissTouchListener;
import rx.Observable;

public class AddRecipeView extends FrameLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    @BindView(R.id.btnAddPhoto)
    Button btnAddPhoto;

    @BindView(R.id.llIngredients)
    LinearLayout llIngredients;

    @BindView(R.id.btnAddIngredient)
    CircleImageView btnAddIngredient;

    @BindView(R.id.etIngredient)
    EditText etIngredient;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.llTags)
    LinearLayout llTags;

    @BindView(R.id.btnAddTag)
    CircleImageView btnAddTag;

    @BindView(R.id.btnAddRecipe)
    Button btnAddRecipe;

    LayoutInflater inflater;

    AddRecipePhotosAdapter photosAdapter;

    public AddRecipeView(AddRecipeActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_add_recipe, this);
        ButterKnife.bind(this);
        photosAdapter = new AddRecipePhotosAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(linearLayoutManager);
        rvPhotos.setAdapter(photosAdapter);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public Observable<Void> addPhotoClick() {
        return RxView.clicks(btnAddPhoto);
    }

    public Observable<Void> addIngredientClick() {
        return RxView.clicks(btnAddIngredient);
    }

    public Observable<Void> addTagClick() {
        return RxView.clicks(btnAddTag);
    }

    public Observable<Void> addRecipeClick(){
        return RxView.clicks(btnAddRecipe);
    }

    public void addTagEditText() {
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

    public List<String> prepareTags(){
        List<String> tags = new ArrayList<>();
        EditText et;
        for(int i=0; i<llTags.getChildCount(); i++){
            et = (EditText) llTags.getChildAt(i);
            tags.add(String.valueOf(et.getText()));
        }
        return tags;
    }

    public void addIngredientEditText() {
        EditText view = (EditText) inflater.inflate(R.layout.view_recipe_edit_text, null);
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

    public List<Ingredient> prepareIngredients(){
        List<Ingredient> ingredients = new ArrayList<>();
        EditText et;
        Ingredient ingredient;
        for(int i=0; i<llIngredients.getChildCount(); i++){
            et = (EditText) llIngredients.getChildAt(i);
            ingredient = new Ingredient();
            ingredient.setName(String.valueOf(et.getText()));
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public void addPhoto(Bitmap photo) {
        photosAdapter.addImage(photo);
    }
}
