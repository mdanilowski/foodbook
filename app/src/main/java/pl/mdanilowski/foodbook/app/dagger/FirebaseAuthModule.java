package pl.mdanilowski.foodbook.app.dagger;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseAuthModule {

    @Provides
    @FoodbookAppScope
    public FirebaseAuth firebaseAuth(){
        return FirebaseAuth.getInstance();
    }
}
