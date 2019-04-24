package pl.mdanilowski.foodbook.activity.following.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.following.FollowingActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = FollowingModule.class, dependencies = FoodbookAppComponent.class)
@FollowingScope
public interface FollowingComponent {

    void inject(FollowingActivity activity);
}
