package pl.mdanilowski.foodbook.activity.followers.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.followers.FollowersActivity;
import pl.mdanilowski.foodbook.activity.followers.mvp.FollowersModel;
import pl.mdanilowski.foodbook.activity.followers.mvp.FollowersPresenter;
import pl.mdanilowski.foodbook.activity.followers.mvp.FollowersView;

@Module
public class FollowersModule {

    FollowersActivity activity;

    public FollowersModule(FollowersActivity activity) {
        this.activity = activity;
    }

    @Provides
    @FollowersScope
    public FollowersActivity activity(){
        return activity;
    }

    @Provides
    @FollowersScope
    public FollowersModel model(){
        return new FollowersModel(activity);
    }

    @Provides
    @FollowersScope
    public FollowersView view(){
        return new FollowersView(activity);
    }

    @Provides
    @FollowersScope
    public FollowersPresenter presenter(FollowersView view, FollowersModel model){
        return new FollowersPresenter(view, model);
    }
}
