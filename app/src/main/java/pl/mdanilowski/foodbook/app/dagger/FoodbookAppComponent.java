package pl.mdanilowski.foodbook.app.dagger;

import com.google.firebase.auth.FirebaseAuth;

import dagger.Component;

@FoodbookAppScope
@Component(modules = FirebaseAuthModule.class)
public interface FoodbookAppComponent {

    FirebaseAuth getFirebaseAuth();

}
