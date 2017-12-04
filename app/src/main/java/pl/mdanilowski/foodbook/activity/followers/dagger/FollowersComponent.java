package pl.mdanilowski.foodbook.activity.followers.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.followers.FollowersActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = FollowersModule.class, dependencies = FoodbookAppComponent.class)
@FollowersScope
public interface FollowersComponent {

    void inject(FollowersActivity activity);
}