package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.content.Context;
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
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.model.RecipeQuery;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.RecipeHolder> {

    List<RecipeQuery> queriedRecipes = new ArrayList<>();
    OnRecipeSearchClickListener listener;
    Context context;

    public RecipeSearchAdapter(Context context, OnRecipeSearchClickListener listener) {
        this.listener = listener;
        this.context = context;
    }

    public void setQueriedRecipes(List<RecipeQuery> queriedRecipes) {
        this.queriedRecipes = queriedRecipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipeSearchAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recipe_search, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeSearchAdapter.RecipeHolder holder, int position) {
        RecipeQuery recipeQuery = queriedRecipes.get(position);
        holder.bindWithListener(recipeQuery, listener);
        if (recipeQuery.getImageUrl() != null) {
            Glide.with(context).load(recipeQuery.getImageUrl()).into(holder.ivFoodImage);
        } else {
            Glide.with(context).load(R.drawable.foodbackgroungplaceholder).into(holder.ivFoodImage);
        }
        if (recipeQuery.getOwnerAvatarUrl() != null) {
            Glide.with(context).load(recipeQuery.getOwnerAvatarUrl()).into(holder.ivOwnerAvatar);
        } else {
            Glide.with(context).load(R.drawable.temp_avatar).into(holder.ivOwnerAvatar);
        }
        holder.tvOwnerName.setText(recipeQuery.getOwnerName());
        holder.tvRecipeName.setText(recipeQuery.getName());
    }

    @Override
    public int getItemCount() {
        return queriedRecipes.size();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivFoodImage)
        ImageView ivFoodImage;
        @BindView(R.id.ivOwnerAvatar)
        CircleImageView ivOwnerAvatar;
        @BindView(R.id.tvOwnerName)
        TextView tvOwnerName;
        @BindView(R.id.tvRecipeName)
        TextView tvRecipeName;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindWithListener(RecipeQuery recipeQuery, OnRecipeSearchClickListener listener) {
            itemView.setOnClickListener(__ -> listener.onClick(recipeQuery));
        }
    }

    public interface OnRecipeSearchClickListener {
        void onClick(RecipeQuery recipeQuery);
    }
}
