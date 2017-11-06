package pl.mdanilowski.foodbook.activity.dashboard.mvp;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.dashboard.DashboardActivity;
import rx.Observable;

@SuppressLint("ViewConstructor")
public class DashboardView extends FrameLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivAvatarImage)
    CircleImageView ivAvatar;

    @BindView(R.id.tvToolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.tlDashboardTabs)
    TabLayout dashboardTabs;

    @BindView(R.id.vpDashboardViewPager)
    ViewPager dashboardViewPager;

    @BindView(R.id.llUploading)
    LinearLayout llUploading;

    @BindView(R.id.pbUploadingImages)
    ProgressBar pbUploadingImages;

    public DashboardView(@NonNull DashboardActivity dashboardActivity) {
        super(dashboardActivity);

        inflate(dashboardActivity, R.layout.activity_dashboard, this);
        ButterKnife.bind(this);
        setToolbar(dashboardActivity);
    }

    private void setToolbar(DashboardActivity dashboardActivity) {
        dashboardActivity.setSupportActionBar(toolbar);
        dashboardActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.home_page);
    }

    public void onCreateOptionsMenu(Menu menu, DashboardActivity activity){
        activity.getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this.getContext(), DashboardActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
    }

    public void setToolbarProfileImage(String uri) {
        Glide.with(toolbar).load(uri).into(ivAvatar);
    }

    public Observable<Void> avatarClick() {
        return RxView.clicks(ivAvatar);
    }

    public void showImageUploadingProgress(){
        llUploading.setVisibility(VISIBLE);
    }

    public void hideImageUploadingProgress(){
        llUploading.setVisibility(GONE);
    }

    public void showSnackBarWithText(String text){
        Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show();
    }
}