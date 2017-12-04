package pl.mdanilowski.foodbook.activity.login.mvp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.app.App;
import rx.Subscription;

public class LoginPresenter extends BasePresenter implements GoogleApiClient.OnConnectionFailedListener {

    private LoginView view;
    private LoginModel model;

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private static final String TAG = "GoogleActivity";
    public static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    public LoginPresenter(LoginView view, LoginModel model, FirebaseAuth firebaseAuth) {
        this.view = view;
        this.model = model;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onCreate() {
        compositeSubscription.add(observeSignInGoogleClick());
        compositeSubscription.add(observeSignInFacebookAttachCallack());

        setUpGoogleSignInUtils();
        setUpFacebookSignInUtils();
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private void setUpGoogleSignInUtils(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(App.getApplicationInstance().getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(model.getActivity())
                .enableAutoManage(model.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setUpFacebookSignInUtils(){
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        Log.d("Success", "Login");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(model.getActivity(), "Anulowano logowanie", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(model.getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private Subscription observeSignInGoogleClick() {
        return view.googleLoginClick().subscribe(__ -> signInGoogle());
    }

    private Subscription observeSignInFacebookAttachCallack(){
        return view.setUpFacebookSignInButton().subscribe(__ ->
                LoginManager.getInstance().logInWithReadPermissions(model.getActivity(), Arrays.asList("public_profile", "user_friends", "email")));
    }

    //Google login code
    private void signInGoogle() {
        Intent singInGoogleIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        model.getActivity().startActivityForResult(singInGoogleIntent, RC_SIGN_IN);
    }

    public void onSignInGoogleActivityResult(int requestCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("ERROR", result.getStatus().toString());
                Toast.makeText(model.getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        ProgressDialog progressDialog = new ProgressDialog(model.getActivity());
        progressDialog.setMessage("Logowanie przez Google");
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(model.getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        model.startDashboardActivity();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(model.getActivity(), "Autoryzacja nie powiodła się",
                                Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.hide();
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(model.getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    //Facebook login code
    public void onFacebookLoginActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        ProgressDialog progressDialog = new ProgressDialog(model.getActivity());
        progressDialog.setMessage("Logowanie przez Facebook");
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(model.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            model.startDashboardActivity();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(model.getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();
                    }
                });
    }
}
