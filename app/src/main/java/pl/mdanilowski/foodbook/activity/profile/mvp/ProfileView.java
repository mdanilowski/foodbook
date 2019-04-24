package pl.mdanilowski.foodbook.activity.profile.mvp;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.profile.ProfileActivity;
import pl.mdanilowski.foodbook.model.User;
import rx.Observable;

@SuppressLint("ViewConstructor")
public class ProfileView extends FrameLayout {

    @BindView(R.id.ivBackgroundImage)
    ImageView ivBackgroundImage;

    @BindView(R.id.ivProfileImage)
    CircleImageView ivProfileImage;

    @BindView(R.id.tvRecipesCount)
    TextView tvRecipesCount;

    @BindView(R.id.stopFollowing)
    LinearLayout stopFollowing;

    @BindView(R.id.follow)
    LinearLayout follow;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvAbout)
    TextView tvAbout;

    @BindView(R.id.tvlocation)
    TextView tvLocation;

    @BindView(R.id.llRecipes)
    LinearLayout llRecipes;

    @BindView(R.id.llFollowers)
    LinearLayout llFollowers;

    @BindView(R.id.toolbarProfile)
    Toolbar toolbar;

    public ProfileView(ProfileActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_profile, this);
        ButterKnife.bind(this);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
    }

    public void setUserData(User currentUser, User userData) {
        if (userData.getBackgroundImage() != null)
            Glide.with(this).load(userData.getBackgroundImage()).into(ivBackgroundImage);
        else ivBackgroundImage.setImageResource(R.color.primary);
        if (userData.getAvatarUrl() != null)
            Glide.with(this).load(userData.getAvatarUrl()).into(ivProfileImage);
        else ivProfileImage.setImageResource(R.color.accent);
        tvRecipesCount.setText(String.valueOf(userData.getRecipesCount()));
        boolean isBeingFollowed = false;
        for (User f : currentUser.getFollowing()) {
            if (f.getUid() != null && f.getUid().equals(userData.getUid())) {
                isBeingFollowed = true;
            }
        }

        if (isBeingFollowed) {
            stopFollowing.setVisibility(VISIBLE);
        } else follow.setVisibility(VISIBLE);
        tvName.setText(userData.getName());
        tvEmail.setText(userData.getEmail());
        tvAbout.setText(userData.getAboutMe());
        tvLocation.setText(String.format("%s, %s", userData.getCountry(), userData.getCity()));
    }

    Observable<Void> clicksRecipes(){
        return RxView.clicks(llRecipes);
    }

    Observable<Void> clicksFollowers() {
        return RxView.clicks(llFollowers);
    }

    Observable<Void> clicksFollow() {
        return RxView.clicks(follow);
    }

    Observable<Void> clicksStopFollowing() {
        return RxView.clicks(stopFollowing);
    }

    void hideFollow(){
        follow.setVisibility(GONE);
        stopFollowing.setVisibility(VISIBLE);
    }

    void showFollow(){
        stopFollowing.setVisibility(GONE);
        follow.setVisibility(VISIBLE);
    }
}
