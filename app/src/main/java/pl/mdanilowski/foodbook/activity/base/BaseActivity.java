package pl.mdanilowski.foodbook.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import pl.mdanilowski.foodbook.app.App;


public class BaseActivity extends AppCompatActivity {

    @Inject
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
