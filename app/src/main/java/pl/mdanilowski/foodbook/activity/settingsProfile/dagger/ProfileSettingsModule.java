package pl.mdanilowski.foodbook.activity.settingsProfile.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.settingsProfile.ProfileSettingsActivity;
import pl.mdanilowski.foodbook.activity.settingsProfile.mvp.ProfileSettingsModel;
import pl.mdanilowski.foodbook.activity.settingsProfile.mvp.ProfileSettingsPresenter;
import pl.mdanilowski.foodbook.activity.settingsProfile.mvp.ProfileSettingsView;

@Module
public class ProfileSettingsModule {

    private ProfileSettingsActivity activity;

    public ProfileSettingsModule(ProfileSettingsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ProfileSettingsScope
    public ProfileSettingsActivity activity(){
        return activity;
    }

    @Provides
    @ProfileSettingsScope
    public ProfileSettingsModel model(){
        return new ProfileSettingsModel(activity);
    }

    @Provides
    @ProfileSettingsScope
    public ProfileSettingsView view(){
        return new ProfileSettingsView(activity);
    }

    @Provides
    @ProfileSettingsScope
    public ProfileSettingsPresenter presenter(ProfileSettingsView view, ProfileSettingsModel model) {
        return new ProfileSettingsPresenter(view,model);
    }
}
