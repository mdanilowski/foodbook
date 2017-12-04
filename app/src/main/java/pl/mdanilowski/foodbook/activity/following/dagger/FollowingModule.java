package pl.mdanilowski.foodbook.activity.following.dagger;

import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.following.FollowingActivity;
import pl.mdanilowski.foodbook.activity.following.mvp.FollowingModel;
import pl.mdanilowski.foodbook.activity.following.mvp.FollowingPresenter;
import pl.mdanilowski.foodbook.activity.following.mvp.FollowingView;

@Module
public class FollowingModule {

    private FollowingActivity activity;

    public FollowingModule(FollowingActivity activity) {
        this.activity = activity;
    }

    @Provides
    @FollowingScope
    public FollowingActivity activity(){
        return activity;
    }

    @Provides
    @FollowingScope
    public FollowingModel model(){
        return new FollowingModel(activity);
    }

    @Provides
    @FollowingScope
    public FollowingView view(){
        return new FollowingView(activity);
    }

    @Provides
    @FollowingScope
    public FollowingPresenter presenter(FollowingView view, FollowingModel model){
        return new FollowingPresenter(view, model);
    }
}
