package pl.mdanilowski.foodbook.activity.findFriends.dagger;


import dagger.Module;
import dagger.Provides;
import pl.mdanilowski.foodbook.activity.findFriends.FindFriendsActivity;
import pl.mdanilowski.foodbook.activity.findFriends.mvp.FindFriendsModel;
import pl.mdanilowski.foodbook.activity.findFriends.mvp.FindFriendsPresenter;
import pl.mdanilowski.foodbook.activity.findFriends.mvp.FindFriendsView;

@Module
public class FindFriendsModule {

    private FindFriendsActivity activity;

    public FindFriendsModule(FindFriendsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @FindFriendsScope
    public FindFriendsActivity activity(){
        return activity;
    }

    @Provides
    @FindFriendsScope
    public FindFriendsModel model(){
        return new FindFriendsModel(activity);
    }

    @Provides
    @FindFriendsScope
    public FindFriendsView view(){
        return new FindFriendsView(activity);
    }

    @Provides
    @FindFriendsScope
    public FindFriendsPresenter presenter(FindFriendsView view, FindFriendsModel model) {
        return new FindFriendsPresenter(view, model);
    }
}
