package pl.mdanilowski.foodbook.activity.addRecipe.mvp;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.mdanilowski.foodbook.activity.addRecipe.AddRecipeActivity;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.Recipe;

public class AddRecipeModel {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private AddRecipeActivity activity;
    public String mCurrentPhotoPath;
    public Uri mImageUri;
    public File tempFile;

    public AddRecipeModel(AddRecipeActivity activity) {
        this.activity = activity;
    }

    public AddRecipeActivity getActivity() {
        return activity;
    }

    public boolean startTakePictureActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                tempFile = createImageFile();
                mImageUri = FileProvider.getUriForFile(activity, App.getApplicationInstance().getPackageName() + ".pl.mdanilowski.foodbook.ext", tempFile);
            } catch (Exception e) {
                Log.v("TAG", "Can't create file to take picture! " + e.getMessage());
                e.printStackTrace();
                Toast.makeText(activity, "Can't take an image ;( Try again later", Toast.LENGTH_SHORT).show();
                return false;
            }
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        return true;
    }

    public void backToDashboard(List<Uri> images, Recipe recipe, boolean wasRecipeAdded) {
        DashboardActivity.start(activity, images, recipe, wasRecipeAdded);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
