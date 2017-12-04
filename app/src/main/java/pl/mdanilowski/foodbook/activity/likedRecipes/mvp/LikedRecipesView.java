package pl.mdanilowski.foodbook.activity.likedRecipes.mvp;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.activity.likedRecipes.LikedRecipesActivity;

public class LikedRecipesView extends FrameLayout {

    @BindView(R.id.toolbarDefault)
    Toolbar toolbar;

    @BindView(R.id.tvToolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.rvLikedRecipes)
    RecyclerView rvLikedRecipes;

    public LikedRecipesView(@NonNull LikedRecipesActivity activity) {
        super(activity);
        inflate(activity, R.layout.activity_liked_recipes, this);
        ButterKnife.bind(this);
        setToolbar(activity);
        initRecyclerView(activity);
    }

    void initRecyclerView(LikedRecipesActivity activity){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvLikedRecipes.setLayoutManager(linearLayoutManager);
    }

    void setAdapterForRecyclerView(RecyclerView.Adapter adapter){
        rvLikedRecipes.setAdapter(adapter);
    }

    private void setToolbar(LikedRecipesActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.recipes_you_liked);
    }
}
