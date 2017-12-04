package pl.mdanilowski.foodbook.activity.settingsProfile.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.settingsProfile.ProfileSettingsActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = ProfileSettingsModule.class, dependencies = FoodbookAppComponent.class)
@ProfileSettingsScope
public interface ProfileSettingsComponent {

    void inject(ProfileSettingsActivity activity);
}
