package pl.mdanilowski.foodbook.activity.addRecipe.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.AddRecipePhotosAdapter;
import pl.mdanilowski.foodbook.utils.ConfirmationDialog;
import pl.mdanilowski.foodbook.utils.SwipeDismissTouchListener;
import rx.Observable;

public class AddRecipeView extends FrameLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvPressToDelete)
    TextView tvPressToDelete;
    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    @BindView(R.id.btnAddPhoto)
    Button btnAddPhoto;
    @BindView(R.id.btnPickImage)
    Button btnPickImage;
    @BindView(R.id.llIngredients)
    LinearLayout llIngredients;
    @BindView(R.id.btnAddIngredient)
    CircleImageView btnAddIngredient;
    @BindView(R.id.tilIngredient)
    TextInputLayout tilIngredient;
    @BindView(R.id.etIngredient)
    TextInputEditText etIngredient;
    @BindView(R.id.tilTag)
    TextInputLayout tilTag;
    @BindView(R.id.etTag)
    TextInputEditText etTag;
    @BindView(R.id.tilDescription)
    TextInputLayout tilDescription;
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
    int deletedImagePosition;

    public AddRecipeView(AddRecipeActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_add_recipe, this);
        ButterKnife.bind(this);
        photosAdapter = new AddRecipePhotosAdapter();
        photosAdapter.setListener(image -> {
            ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(activity.getResources().getString(R.string.deleting_photo),
                    activity.getResources().getString(R.string.sure_delete_photo),
                    new ConfirmationDialog.OnClickResult() {
                @Override
                public void onPositiveClick() {
                    deletedImagePosition = photosAdapter.removeImage(image);
                    activity.deleteImage(deletedImagePosition);
                }

                @Override
                public void onNegativeClick() {

                }
            });
            confirmationDialog.show(activity.getSupportFragmentManager(), "DELETE_PHOTO_DIALOG");
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rvPhotos.setLayoutManager(linearLayoutManager);
        rvPhotos.setAdapter(photosAdapter);
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        tvToolbarTitle.setText(getResources().getString(R.string.add_recipe));
    }

    public Observable<Void> addPhotoClick() {
        return RxView.clicks(btnAddPhoto);
    }

    public Observable<Void> pickImageClick() {
        return RxView.clicks(btnPickImage);
    }

    public Observable<Void> addIngredientClick() {
        return RxView.clicks(btnAddIngredient);
    }

    public Observable<Void> addTagClick() {
        return RxView.clicks(btnAddTag);
    }

    public Observable<Void> addRecipeClick() {
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

    public void addIngredientEditText() {
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

    public void addPhoto(Bitmap photo) {
        tvPressToDelete.setVisibility(VISIBLE);
        photosAdapter.addImage(photo);
    }

    public void displayNotFilled(TextInputLayout til) {
        til.setError(getContext().getString(R.string.must_be_filled));
    }

    public void closeError(TextInputLayout til) {
        til.setErrorEnabled(false);
    }
}