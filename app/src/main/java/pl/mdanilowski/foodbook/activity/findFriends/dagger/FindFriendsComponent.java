package pl.mdanilowski.foodbook.activity.findFriends.dagger;

import dagger.Component;
import pl.mdanilowski.foodbook.activity.findFriends.FindFriendsActivity;
import pl.mdanilowski.foodbook.app.dagger.FoodbookAppComponent;

@Component(modules = FindFriendsModule.class, dependencies = FoodbookAppComponent.class)
@FindFriendsScope
public interface FindFriendsComponent {

    void inject(FindFriendsActivity activity);
}
