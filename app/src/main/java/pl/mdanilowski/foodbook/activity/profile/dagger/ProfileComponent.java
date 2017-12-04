package pl.mdanilowski.foodbook.activity.profile.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@ProfileScope
@Component(modules = ProfileModule.class, dependencies = FoodbookAppComponent.class)
public interface ProfileComponent {

    void inject(ProfileActivity activity);
}
