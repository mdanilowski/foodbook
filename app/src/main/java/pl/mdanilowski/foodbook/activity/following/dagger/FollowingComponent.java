package pl.mdanilowski.foodbook.activity.following.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.following.FollowingActivity;

@Component(modules = FollowingModule.class)
@FollowingScope
public interface FollowingComponent {

    void inject(FollowingActivity activity);
}
