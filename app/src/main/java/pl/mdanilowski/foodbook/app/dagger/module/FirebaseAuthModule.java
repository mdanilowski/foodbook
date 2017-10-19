package pl.mdanilowski.foodbook.app.dagger.module;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppScope;

@Module
public class FirebaseAuthModule {

    @Provides
    @FoodbookAppScope
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @FoodbookAppScope
    public FirebaseFirestore firebaseFirestore() {
        FirebaseFirestore.setLoggingEnabled(true);
        return FirebaseFirestore.getInstance();
    }
}
