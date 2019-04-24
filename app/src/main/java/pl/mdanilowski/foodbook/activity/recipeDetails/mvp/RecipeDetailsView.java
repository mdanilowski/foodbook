package pl.mdanilowski.foodbook.activity.recipeDetails.mvp;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.recipeDetails.RecipeDetailsActivity;
import pl.mdanilowski.foodbook.adapter.pagerAdapters.RecipePagerAdapter;
import rx.Observable;

public class RecipeDetailsView extends FrameLayout {

    @BindView(R.id.toolbarDefault)
    Toolbar toolbar;

    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;

    @BindView(R.id.vpRecipeImages)
    ViewPager vpRecipeImages;

    @BindView(R.id.tabDots)
    TabLayout tabDots;

    @BindView(R.id.tvRecipeName)
    TextView tvRecipeName;

    @BindView(R.id.tvIngredients)
    TextView tvIngredients;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvTags)
    TextView tvTags;

    @BindView(R.id.rvComments)
    RecyclerView rvComments;

    @BindView(R.id.ivShare)
    ImageView ivShare;

    @BindView(R.id.ivLike)
    ImageView ivLike;

    @BindView(R.id.ivUnlike)
    ImageView ivUnlike;

    @BindView(R.id.ivComment)
    ImageView ivComment;

    @BindView(R.id.tvLikesCount)
    TextView tvLikesCount;

    @BindView(R.id.tvCommentCount)
    TextView tvCommentsCount;

    @BindView(R.id.ivUser)
    ImageView ivUser;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.llOwner)
    LinearLayout llOwner;

    RecipePagerAdapter recipePagerAdapter;

    public RecipeDetailsView(@NonNull RecipeDetailsActivity recipeDetailsActivity) {
        super(recipeDetailsActivity);
        inflate(recipeDetailsActivity, R.layout.activity_recipe_details, this);
        ButterKnife.bind(this);

        recipePagerAdapter = new RecipePagerAdapter(recipeDetailsActivity);

        vpRecipeImages.setAdapter(recipePagerAdapter);
        tabDots.setupWithViewPager(vpRecipeImages);
        recipeDetailsActivity.setSupportActionBar(toolbar);
        recipeDetailsActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recipeDetailsActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        recipeDetailsActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle.setText(R.string.recipe);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);

        rvComments.setFocusable(false);
        tvRecipeName.requestFocus();
    }

    public void setPagerImages(List<String> images) {
        recipePagerAdapter.setImages(images);
    }

    public void setOwnerAvatar(String imageUrl) {
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(ivUser);
        }
    }

    public void setOwnerName(String name) {
        tvName.setText(name);
    }

    public void setClickListenerOnOwner(OnClickListener listener) {
        llOwner.setOnClickListener(listener);
    }

    public void setTvRecipeName(String recipeName) {
        this.tvRecipeName.setText(recipeName);
    }

    public void setTvIngredients(List<String> ingredients) {
        StringBuilder formattedIngredients = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            formattedIngredients.append(ingredients.get(i));
            if (i != ingredients.size() - 1)
                formattedIngredients.append("\n");
        }
        this.tvIngredients.setText(formattedIngredients.toString());
    }

    public void setTvDescription(String description) {
        this.tvDescription.setText(description);
    }

    public void setTvTags(List<String> tags) {
        StringBuilder formattedTags = new StringBuilder();
        for (String s : tags) {
            formattedTags.append(s);
            formattedTags.append("\n");
        }
        this.tvTags.setText(formattedTags.toString());
    }

    @SuppressLint("DefaultLocale")
    public void setLikesCount(int count) {
        tvLikesCount.setText(String.format("%d %s", count, getContext().getString(R.string.likes)));
    }

    @SuppressLint("DefaultLocale")
    public void setCommentsCount(long count) {
        tvCommentsCount.setText(String.format("%d %s", count, getContext().getString(R.string.comments)));
    }

    public void setRecipeLiked() {
        ivLike.setVisibility(GONE);
        ivUnlike.setVisibility(VISIBLE);
        unlikeClick();
        likeClick();
    }

    public void setRecipeNotLiked() {
        ivUnlike.setVisibility(GONE);
        ivLike.setVisibility(VISIBLE);
        likeClick();
        unlikeClick();
    }

    public void setNoRecipeContent() {

    }

    Observable<Void> likeClick() {
        return RxView.clicks(ivLike);
    }

    Observable<Void> unlikeClick() {
        return RxView.clicks(ivUnlike);
    }

    Observable<Void> commentClick() {
        return RxView.clicks(ivComment);
    }

    Observable<Void> shareClick() {
        return RxView.clicks(ivShare);
    }
}
