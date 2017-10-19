package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.Drawer;

import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.base.BasePresenter;
import pl.mdanilowski.foodbook.adapter.pagerAdapters.DashboardFragmentsPagerAdapter;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.utils.MaterialDrawerBuilder;
import rx.Subscription;

public class DashboardPresenter extends BasePresenter {

    private DashboardView view;
    private DashboardModel model;
    FirebaseUser user;
    Drawer drawer;

    public DashboardPresenter(DashboardModel model, DashboardView view, FirebaseUser user) {
        this.model = model;
        this.view = view;
        this.user = user;
    }

    @Override
    public void onCreate() {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        compositeSubscription.add(observeAvatarClick());
        setViewPagerAndTabs();
        setToolbarAndDrawer();
    }

    @Override
    public void onDestroy() {
        compositeSubscription.clear();
    }

    private Subscription observeAvatarClick() {
        return view.avatarClick().subscribe(__ -> drawer.openDrawer());
    }

    private void setToolbarAndDrawer() {
        view.setToolbarProfileImage(user.getPhotoUrl().toString());
        drawer = MaterialDrawerBuilder.setDrawer(model.getActivity(), view.toolbar, user);
    }

    private void setViewPagerAndTabs() {
        view.dashboardViewPager.setAdapter(new DashboardFragmentsPagerAdapter(model.getFragmentManager()));
        view.dashboardTabs.setupWithViewPager(view.dashboardViewPager);
        setTabIcons();
    }

    private void setTabIcons() {
        view.dashboardTabs.getTabAt(0).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(1).setIcon(R.mipmap.home_icon);
        view.dashboardTabs.getTabAt(2).setIcon(R.mipmap.search_icon);
        view.dashboardTabs.getTabAt(3).setIcon(R.mipmap.search_icon);
    }

    public void onFragmentInteraction(Uri uri) {

    }
}
