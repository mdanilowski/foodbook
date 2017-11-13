package pl.mdanilowski.foodbook.activity.usersRecipes.mvp;


import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.usersRecipes.UsersRecipesActivity;
import pl.mdanilowski.foodbook.adapter.recyclerAdapters.RecipesAdapter;
import pl.mdanilowski.foodbook.fragment.dashboard.RecipesFragment;
import pl.mdanilowski.foodbook.model.Recipe;
import pl.mdanilowski.foodbook.model.User;

public class UsersRecipesView extends FrameLayout {

    @BindView(R.id.ivBackgroundImage)
    ImageView ivBackgroundImage;

    @BindView(R.id.ivProfileImage)
    CircleImageView ivProfileImage;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.toolbarProfile)
    Toolbar toolbar;

    @BindView(R.id.rvRecipes)
    RecyclerView rvRecipes;

    RecipesAdapter recipesAdapter;
    RecipesFragment.OnAdapterItemClickListener listener;

    public UsersRecipesView(UsersRecipesActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_users_recipes, this);
        ButterKnife.bind(this);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);

        recipesAdapter = new RecipesAdapter(activity, recipe -> listener.onAdapterItemClick(recipe));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvRecipes.setLayoutManager(linearLayoutManager);
        rvRecipes.setAdapter(recipesAdapter);
    }

    public void setListener(RecipesFragment.OnAdapterItemClickListener listener) {
        this.listener = listener;
    }

    public void addItemToAdapter(Recipe recipe) {
        recipesAdapter.addRecipe(recipe);
        scrollAdapterUp();
    }

    private void scrollAdapterUp() {
        new Handler().postDelayed(() -> rvRecipes.smoothScrollToPosition(0), 1);
    }

    public void setUserData(User userData) {
        if (userData.getBackgroundImage() != null)
            Glide.with(this).load(userData.getBackgroundImage()).into(ivBackgroundImage);
        else ivBackgroundImage.setImageResource(R.color.primary);
        if (userData.getAvatarUrl() != null)
            Glide.with(this).load(userData.getAvatarUrl()).into(ivProfileImage);
        else ivProfileImage.setImageResource(R.color.accent);
        tvName.setText(userData.getName());
    }
}
