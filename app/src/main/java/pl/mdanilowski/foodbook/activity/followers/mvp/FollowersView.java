package pl.mdanilowski.foodbook.activity.followers.mvp;


import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.followers.FollowersActivity;

public class FollowersView extends FrameLayout {

    @BindView(R.id.toolbarDefault)
    Toolbar toolbar;

    @BindView(R.id.tvToolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.rvFollowers)
    RecyclerView rvFollowers;

    public FollowersView(@NonNull FollowersActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_followers, this);
        ButterKnife.bind(this);
        setToolbar(activity);
        initRecyclerView(activity);
    }

    void initRecyclerView(FollowersActivity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvFollowers.setLayoutManager(linearLayoutManager);
    }

    void setAdapterForRecyclerView(RecyclerView.Adapter adapterForRecyclerView){
        rvFollowers.setAdapter(adapterForRecyclerView);
    }

    private void setToolbar(FollowersActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        toolbarTitle.setText(R.string.following_you);
    }
}
