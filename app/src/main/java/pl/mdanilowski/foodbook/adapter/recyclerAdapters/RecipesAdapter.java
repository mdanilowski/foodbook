package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.model.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private Fragment fragment;

    public RecipesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(0, recipe);
        notifyItemInserted(0);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_recipe, parent, false);
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvFoodName.setText(recipe.getName());
        holder.tvLikesCount.setText(String.valueOf(recipe.getLikes()));
        holder.tvCommentCount.setText(String.valueOf(recipe.getComments().size()));
        if (recipe.getPhotosUrls() != null && !recipe.getPhotosUrls().isEmpty())
            Glide.with(fragment).load(recipe.getPhotosUrls().get(0)).into(holder.ivFoodImage);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFoodImage)
        ImageView ivFoodImage;

        @BindView(R.id.tvFoodName)
        TextView tvFoodName;

        @BindView(R.id.tvLikesCount)
        TextView tvLikesCount;

        @BindView(R.id.tvCommentCount)
        TextView tvCommentCount;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
