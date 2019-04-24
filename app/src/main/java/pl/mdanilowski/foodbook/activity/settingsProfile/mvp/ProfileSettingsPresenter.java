package pl.mdanilowski.foodbook.activity.settingsProfile.mvp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import rx.Subscription;

public class ProfileSettingsPresenter extends BasePresenter {

    private static int PICK_BACKGROUND_IMAGE_REQUEST_CODE = 100;
    private static int PICK_AVATAR_IMAGE_REQUEST_CODE = 101;

    private Uri uriForAvatar = null;
    private Uri uriForBackground = null;

    private ProfileSettingsView view;
    private ProfileSettingsModel model;
    private User foodbookUser;

    public ProfileSettingsPresenter(ProfileSettingsView view, ProfileSettingsModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        foodbookUser = model.getUserFromIntent();
        compositeSubscription.add(observeChangeBackgroundImageClick());
        compositeSubscription.add(observeChangeAvatarClick());
        compositeSubscription.add(observeSaveSettingsClick());
        view.setUserContent(foodbookUser);
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeChangeBackgroundImageClick() {
        return view.changeBackgroundClicks().subscribe(__ -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            model.getActivity().startActivityForResult(Intent.createChooser(intent, view.getResources().getString(R.string.select_picture)), PICK_BACKGROUND_IMAGE_REQUEST_CODE);
        });
    }

    private Subscription observeChangeAvatarClick() {
        return view.changeAvatarImageClick().subscribe(__ -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            model.getActivity().startActivityForResult(Intent.createChooser(intent, view.getResources().getString(R.string.select_picture)), PICK_AVATAR_IMAGE_REQUEST_CODE);
        });
    }

    private Subscription observeSaveSettingsClick() {
        return view.saveSettingsClick().subscribe(__ -> {
            foodbookUser.setName(String.valueOf(view.etName.getText()));
            foodbookUser.setAboutMe(String.valueOf(view.etAboutMe.getText()));
            foodbookUser.setEmail(String.valueOf(view.etEmail.getText()));
            foodbookUser.setCountry(String.valueOf(view.etCountry.getText()));
            foodbookUser.setCity(String.valueOf(view.etCity.getText()));
            foodBookSimpleStorage.saveUser(foodbookUser);

            model.finishActivity(uriForAvatar, uriForBackground, foodbookUser);
            Toast.makeText(App.getApplicationInstance(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream inputStream = null;
        Bitmap bitmap;
        if (requestCode == PICK_BACKGROUND_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (data.getData() != null) {
                    uriForBackground = data.getData();
                }
                inputStream = model.getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                view.ivBackgroundImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), R.string.ups_something_wrong, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_AVATAR_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                if (data.getData() != null) {
                    uriForAvatar = data.getData();
                }
                inputStream = model.getActivity().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                view.ivAvatarImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), R.string.ups_something_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
