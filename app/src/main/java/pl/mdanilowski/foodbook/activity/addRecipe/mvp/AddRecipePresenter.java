package pl.mdanilowski.foodbook.activity.addRecipe.mvp;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.RecipeQuery;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.InformationDialog;
import rx.Subscription;

public class AddRecipePresenter extends BasePresenter {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;

    private AddRecipeView view;
    private AddRecipeModel model;
    FirebaseUser user;
    List<Uri> imageList = new ArrayList<>();
    private int imageCounter = 0;


    public AddRecipePresenter(AddRecipeView view, AddRecipeModel model) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        user = firebaseAuth.getCurrentUser();
        compositeSubscription.add(observeAddPhotoClick());
        compositeSubscription.add(observePickImageClick());
        compositeSubscription.add(observeAddIngredientClick());
        compositeSubscription.add(observeAddTagClick());
        compositeSubscription.add(observeAddRecipeClick());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeAddIngredientClick() {
        return view.addIngredientClick().subscribe(__ -> view.addIngredientEditText());
    }

    private Subscription observeAddTagClick() {
        return view.addTagClick().subscribe(__ -> view.addTagEditText());
    }

    private Subscription observeAddPhotoClick() {
        return view.addPhotoClick().subscribe(__ -> {
            if (imageCounter < 5) {
                model.startTakePictureActivity();
            } else {
                InformationDialog.newInstance(view.getResources().getString(R.string.cant_add_photo), view.getResources().getString(R.string.max_photos), null)
                        .show(model.getActivity().getSupportFragmentManager(), "PHOTO_LIMIT_DIALOG");
            }
        });
    }

    private Subscription observePickImageClick() {
        return view.pickImageClick().subscribe(__ -> {
            if(imageCounter < 5) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                model.getActivity().startActivityForResult(Intent.createChooser(intent, view.getResources().getString(R.string.select_picture)), REQUEST_PICK_IMAGE);
            }else {
                InformationDialog.newInstance(view.getResources().getString(R.string.cant_add_photo), view.getResources().getString(R.string.max_photos), null)
                .show(model.getActivity().getSupportFragmentManager(), "IMAGE_LIMIT_DIALOG");
            }
        });
    }

    private Subscription observeAddRecipeClick() {
        return view.addRecipeClick().subscribe(__ -> {

            if (validateForm()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

                String name = String.valueOf(view.etName.getText());
                String description = String.valueOf(view.etDescription.getText());
                List<String> tags = view.prepareTags();

                Map<String, Boolean> queryStringsMap = new HashMap<>();
                String[] nameStrings = name.split(" ");
                List<String> queryStringArray = new ArrayList<>();
                Collections.addAll(queryStringArray, nameStrings);
                queryStringArray.addAll(tags);

                for (String s : queryStringArray) {
                    queryStringsMap.put(s.toLowerCase(), true);
                }

                User foodbookUser = foodBookSimpleStorage.getUser();
                RecipeQuery recipeQuery = new RecipeQuery(queryStringsMap, null, name, null, foodbookUser.getUid(), foodbookUser.getAvatarUrl(), foodbookUser.getName());

                Recipe recipe = new Recipe(user.getUid(),
                        name,
                        description,
                        0,
                        foodBookSimpleStorage.getUser().getName(),
                        true,
                        calendar.getTime(),
                        view.prepareIngredients(),
                        tags,
                        new ArrayList<String>());
                model.backToDashboard(imageList, recipe, recipeQuery, true);
            }
        });
    }

    private boolean validateForm() {
        boolean isFilled = true;
        if (view.etName.getText().toString().isEmpty()) {
            view.displayNotFilled(view.tilName);
            isFilled = false;
        } else view.closeError(view.tilName);
        if (view.etDescription.getText().toString().isEmpty()) {
            view.displayNotFilled(view.tilDescription);
            isFilled = false;
        } else view.closeError(view.tilDescription);
        if (view.etIngredient.getText().toString().isEmpty()) {
            view.displayNotFilled(view.tilIngredient);
            isFilled = false;
        } else view.closeError(view.tilIngredient);
        if (view.etTag.getText().toString().isEmpty()) {
            view.displayNotFilled(view.tilTag);
            isFilled = false;
        } else view.closeError(view.tilTag);
        return isFilled;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            model.getActivity().getContentResolver().notifyChange(model.mImageUri, null);
            ContentResolver cr = model.getActivity().getContentResolver();
            Bitmap photo;
            try {
                photo = android.provider.MediaStore.Images.Media.getBitmap(cr, model.mImageUri);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 40, out);
                Bitmap temp = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                Bitmap imageToShow = Bitmap.createScaledBitmap(temp, photo.getWidth() / 3, photo.getHeight() / 3, false);
                view.addPhoto(imageToShow);
                imageList.add(model.mImageUri);
                imageCounter++;
            } catch (Exception e) {
                Toast.makeText(model.getActivity(), R.string.failed_to_load, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                if (data.getData() != null) {
                    imageList.add(data.getData());
                }
                InputStream inputStream = model.getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                view.addPhoto(bitmap);
                imageCounter++;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), R.string.ups_something_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteImage(int deletedImagePosition) {
        imageList.remove(deletedImagePosition);
        imageCounter--;
        if (imageList.isEmpty()) {
            view.tvPressToDelete.setVisibility(View.GONE);
        }
    }
}
