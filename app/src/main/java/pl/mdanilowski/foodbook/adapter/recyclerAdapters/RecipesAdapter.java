package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.model.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private Context context;
    OnRecipeClickListener listener;

    public RecipesAdapter(Fragment context, OnRecipeClickListener listener) {
        this.context = context.getContext();
        this.listener = listener;
    }

    public RecipesAdapter(Activity context, OnRecipeClickListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void setRecipes(List<Recipe> recipes) {
        this.recipes.clear();
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(0, recipe);
        Collections.sort(recipes, (o1, o2) -> {
            if (o1.getAddDate().before(o2.getAddDate())) return 1;
            else return -1;
        });
        notifyItemInserted(0);
    }

    public void updateRecipe(Recipe recipe) {
        for (Recipe r : recipes) {
            if (r.getRid().equals(recipe.getRid())) {
                recipes.set(recipes.indexOf(r), recipe);
                notifyDataSetChanged();
            }
        }
    }

    public void deleteRecipe(Recipe recipe) {
        Iterator<Recipe> iterator = recipes.iterator();
        while (iterator.hasNext()) {
            Recipe r = iterator.next();
            if (r.getRid().equals(recipe.getRid())) {
                iterator.remove();
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_recipe, parent, false);
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, listener);
        holder.tvFoodName.setText(recipe.getName());
        holder.tvLikesCount.setText(String.valueOf(recipe.getLikes()));
        holder.tvCommentCount.setText(String.valueOf(recipe.getCommentCount()));

        StringBuilder tagsBuilder = new StringBuilder();
        if (recipe.getTags() != null && !recipe.getTags().isEmpty()) {
            tagsBuilder.append(recipe.getTags().get(0));
        }
        for (int i = 1; i < recipe.getTags().size(); i++) {
            tagsBuilder.append(String.format(", %s", recipe.getTags().get(i)));
        }
        holder.tvRecipeTags.setText(tagsBuilder.toString());

        if (recipe.getPhotosUrls() != null && !recipe.getPhotosUrls().isEmpty())
            Glide.with(context).load(recipe.getPhotosUrls().get(0)).into(holder.ivFoodImage);
        else
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.foodbackgroungplaceholder)).into(holder.ivFoodImage);
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

        @BindView(R.id.tvRecipeTags)
        TextView tvRecipeTags;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Recipe recipe, OnRecipeClickListener listener) {
            itemView.setOnClickListener(__ -> listener.onRecipeClick(recipe));
        }
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }
}
