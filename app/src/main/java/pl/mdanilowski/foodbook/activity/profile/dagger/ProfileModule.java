package pl.mdanilowski.foodbook.activity.profile.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.activity.profile.mvp.ProfileModel;
import pl.mdanilowski.foodbook.activity.profile.mvp.ProfilePresenter;
import pl.mdanilowski.foodbook.activity.profile.mvp.ProfileView;

@Module
public class ProfileModule {

    private ProfileActivity activity;

    public ProfileModule(ProfileActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ProfileScope
    public ProfileActivity getActivity() {
        return activity;
    }

    @Provides
    @ProfileScope
    public ProfileModel model() {
        return new ProfileModel(activity);
    }


    @Provides
    @ProfileScope
    public ProfileView view() {
        return new ProfileView(activity);
    }


    @Provides
    @ProfileScope
    public ProfilePresenter presenter(ProfileModel model, ProfileView view) {
        return new ProfilePresenter(model, view);
    }
}
