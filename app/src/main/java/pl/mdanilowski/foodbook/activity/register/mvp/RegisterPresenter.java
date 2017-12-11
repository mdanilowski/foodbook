package pl.mdanilowski.foodbook.activity.register.mvp;


import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import rx.Subscription;

public class RegisterPresenter extends BasePresenter {

    private final RegisterView view;
    private final RegisterModel model;

    public RegisterPresenter(RegisterView view, RegisterModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        compositeSubscription.add(observeRegisterClick());
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeRegisterClick() {
        return view.registerClick().subscribe(__ -> {
            if (validateRegisterForm()) {
                String email = String.valueOf(view.etEmail.getText());
                String password = String.valueOf(view.etPassword.getText());
                String name = String.valueOf(view.etName.getText());
                String country = String.valueOf(view.etCountry.getText());
                String city = String.valueOf(view.etCity.getText());
                String aboutMe = String.valueOf(view.etAboutMe.getText());
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(model.getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("REGISTERED", "signInWithEmail:success");
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    Log.d("REG_ID", firebaseUser.getUid());
                                    User user = new User();
                                    user.setEmail(email);
                                    user.setName(name);
                                    user.setCountry(country);
                                    user.setCity(city);
                                    user.setAboutMe(aboutMe);
                                    user.setUid(firebaseUser.getUid());
                                    if (firebaseUser.getPhotoUrl() != null) {
                                        user.setAvatarUrl(firebaseUser.getPhotoUrl().toString());
                                    }
                                    model.startDashboardAfterRegister(user);
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(view.getContext(), "The email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                                    } else if (task.getException() instanceof FirebaseNetworkException) {
                                        Toast.makeText(view.getContext(), "You are offline. Check connection and try again.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(view.getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.w("ERROR_REG", "signInWithEmail:failure", task.getException());
                                }
                            }
                        });
            }
        });
    }

    private boolean validateRegisterForm() {
        boolean isValid = true;
        if (TextUtils.isEmpty(view.etEmail.getText())) {
            isValid = false;
            view.displayInputEmptyError(view.tilEmail);
        } else {
            view.hideInputEmptyError(view.tilEmail);
        }
        if (TextUtils.isEmpty(view.etPassword.getText())) {
            isValid = false;
            view.displayInputEmptyError(view.tilPassword);
        } else {
            view.hideInputEmptyError(view.tilPassword);
        }
        if (TextUtils.isEmpty(view.etPasswordConfirm.getText())) {
            isValid = false;
            view.displayInputEmptyError(view.tilPasswordConfirm);
        } else {
            view.hideInputEmptyError(view.tilPasswordConfirm);
        }
        if (!view.etPassword.getText().toString().equals(view.etPasswordConfirm.getText().toString())) {
            view.showPasswordDoNotMatch();
            isValid = false;
        } else {
            view.hideInputEmptyError(view.tilPasswordConfirm);
        }
        return isValid;
    }
}