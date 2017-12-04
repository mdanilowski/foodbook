package pl.mdanilowski.foodbook.activity.addRecipe.mvp;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Comment;
import pl.mdanilowski.foodbook.model.Recipe;
import rx.Subscription;

public class AddRecipePresenter extends BasePresenter {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private AddRecipeView view;
    private AddRecipeModel model;
    FirebaseUser user;
    List<Uri> imageList = new ArrayList<>();


    public AddRecipePresenter(AddRecipeView view, AddRecipeModel model) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.view = view;
        this.model = model;
    }


    @Override
    public void onCreate() {
        user = firebaseAuth.getCurrentUser();
        compositeSubscription.add(observeAddPhotoClick());
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
        return view.addPhotoClick().subscribe(__ -> model.startTakePictureActivity());
    }

    private Subscription observeAddRecipeClick() {
        return view.addRecipeClick().subscribe(__ -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            Recipe recipe = new Recipe(String.valueOf(view.etName.getText()),
                    String.valueOf(view.etDescription.getText()),
                    0,
                    true,
                    calendar.getTime(),
                    view.prepareIngredients(),
                    new ArrayList<Comment>(),
                    view.prepareTags(),
                    new ArrayList<String>());
            model.backToDashboard(imageList, recipe, true);
        });
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
                Bitmap imageToShow = Bitmap.createScaledBitmap(temp, photo.getWidth()/3, photo.getHeight()/3, false);
                view.addPhoto(imageToShow);
                imageList.add(model.mImageUri);
            } catch (Exception e) {
                Toast.makeText(model.getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Failed to load", e);
            }
        }
    }
}
