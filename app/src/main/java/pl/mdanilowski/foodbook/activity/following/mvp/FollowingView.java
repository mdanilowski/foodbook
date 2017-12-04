package pl.mdanilowski.foodbook.activity.following.mvp;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.following.FollowingActivity;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.FollowAdapter;
import pl.mdanilowski.foodbook.model.User;

public class FollowingView extends FrameLayout {

    @BindView(R.id.toolbarDefault)
    Toolbar toolbar;

    @BindView(R.id.tvToolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.rvFollowing)
    RecyclerView rvFollowing;

    FollowAdapter followAdapter;

    public FollowingView(@NonNull FollowingActivity context) {
        super(context);
        inflate(context, R.layout.activity_following, this);
        ButterKnife.bind(this);
        setToolbar(context);
        initAdapter(context);
    }

    private void initAdapter(FollowingActivity context){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        followAdapter = new FollowAdapter(user -> ProfileActivity.start(context, user.getUid()));
        rvFollowing.setLayoutManager(linearLayoutManager);
        rvFollowing.setAdapter(followAdapter);
    }

    void setFollowingUsers(List<User> users){
        followAdapter.setFollowingUsers(users);
    }

    private void setToolbar(FollowingActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.following_title);
    }
}
