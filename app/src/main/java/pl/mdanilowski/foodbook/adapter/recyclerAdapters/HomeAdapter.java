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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.app.dagger.module.GlideApp;
import pl.mdanilowski.foodbook.model.userUpdates.MyRecipeNewComment;
import pl.mdanilowski.foodbook.model.userUpdates.UpdateType;
import pl.mdanilowski.foodbook.model.userUpdates.UserUpdate;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.UserUpdateHolder> {

    private List<UserUpdate> userUpdatesList = new ArrayList<>();
    private Context context;
    private OnMessageItemClickListener listener;

    public HomeAdapter(Context context, OnMessageItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void addUpdate(UserUpdate update) {
        userUpdatesList.add(0, update);
        notifyItemInserted(0);
        Collections.sort(userUpdatesList, (o1, o2) -> {
            if (o1.getAddDate() > o2.getAddDate()) return -1;
            else return 1;
        });
        notifyDataSetChanged();
    }

    public void removeUpdate(UserUpdate update) {
        switch (update.getUpdateType()) {
            case FOLLOWERS_NEW_COMMENT:
            case MY_RECIPE_NEW_COMMENT:
                Iterator<UserUpdate> iterator = userUpdatesList.iterator();
                while (iterator.hasNext()) {
                    UserUpdate updateHolder = iterator.next();
                    if (updateHolder.getUpdateType() == UpdateType.MY_RECIPE_NEW_COMMENT || updateHolder.getUpdateType() == UpdateType.FOLLOWERS_NEW_COMMENT) {
                        if (updateHolder.getCid().equals(update.getCid())) {
                            iterator.remove();
                            notifyDataSetChanged();
                        }
                    }
                }
                break;
            case MY_RECIPE_LIKE:
            case FOLLOWERS_NEW_RECIPE:
            case FOLLOWERS_NEW_LIKE:
                Iterator<UserUpdate> iterator2 = userUpdatesList.iterator();
                while (iterator2.hasNext()) {
                    UserUpdate updateHolder = iterator2.next();
                    if (updateHolder.getUpdateType() == UpdateType.MY_RECIPE_LIKE ||
                            updateHolder.getUpdateType() == UpdateType.FOLLOWERS_NEW_RECIPE ||
                            updateHolder.getUpdateType() == UpdateType.FOLLOWERS_NEW_LIKE) {
                        if (updateHolder.getRid().equals(update.getRid()) && updateHolder.getUid().equals(update.getUid())) {
                            iterator2.remove();
                            notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

    public void modifyUpdate(UserUpdate update) {
        switch (update.getUpdateType()) {
            case FOLLOWERS_NEW_COMMENT:
            case MY_RECIPE_NEW_COMMENT:
                for (int i = 0; i < userUpdatesList.size(); i++) {
                    if (userUpdatesList.get(i).getCid() == update.getCid()) {

                    }
                }
                break;
            case MY_RECIPE_LIKE:
            case FOLLOWERS_NEW_RECIPE:
            case FOLLOWERS_NEW_LIKE:
                for (int i = 0; i < userUpdatesList.size(); i++) {
                    if (userUpdatesList.get(i).getRid().equals(update.getRid()) && userUpdatesList.get(i).getUid().equals(update.getUid())) {
                        userUpdatesList.set(i, update);
                        notifyDataSetChanged();
                    }
                }

                break;
        }
    }

    public void removeMyRecipeComment(MyRecipeNewComment myRecipeNewComment) {
        //TODO
    }

    @Override
    public UserUpdateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_default_user_update, parent, false);
                return new UserUpdateHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(UserUpdateHolder holder, int position) {
        UserUpdate update = userUpdatesList.get(position);
        holder.bindWithClickListener(update, listener);
        if (update.getUserAvatar() != null) {
            GlideApp.with(holder.itemView).load(update.getUserAvatar()).placeholder(context.getResources().getDrawable(R.drawable.temp_avatar)).into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageResource(R.drawable.temp_avatar);
        }
        if (update.getRecipeImage() != null) {
            GlideApp.with(holder.itemView).load(update.getRecipeImage()).placeholder(context.getResources().getDrawable(R.drawable.placeholder_food_image)).into(holder.ivFoodImage);
        } else {
            Glide.with(holder.itemView).load(context.getResources().getDrawable(R.drawable.foodbackgroungplaceholder)).into(holder.ivFoodImage);
        }
        holder.tvFoodName.setText(update.getRecipeName());

        switch (update.getUpdateType()) {
            case MY_RECIPE_LIKE:
                holder.tvMessageText.setText(String.format("%s %s", update.getUserName(), context.getResources().getString(R.string.likes_your_recipe)));
                break;
            case FOLLOWERS_NEW_LIKE:
                holder.tvMessageText.setText(String.format("%s %s", update.getUserName(), context.getResources().getString(R.string.likes_recipe)));
                break;
            case FOLLOWERS_NEW_RECIPE:
                holder.tvMessageText.setText(String.format("%s %s", update.getUserName(), context.getResources().getString(R.string.added_new_recipe)));
                break;
            case FOLLOWERS_NEW_COMMENT:
                if(update.getUid().equals(update.getOid())){
                    holder.tvMessageText.setText(String.format("%s %s", update.getUserName(), "skomentował swój przepis"));
                }else {
                    holder.tvMessageText.setText(String.format("%s %s %s", update.getUserName(), context.getResources().getString(R.string.commented_on_recipe), update.getRecipeOwnerName()));
                }
                break;
            case MY_RECIPE_NEW_COMMENT:
                if (!update.getUid().equals(update.getOid())) {
                    holder.tvMessageText.setText(String.format("%s %s %s", context.getResources().getString(R.string.user), update.getUserName(), context.getResources().getString(R.string.commented_your_recipe)));
                }else {
                    holder.tvMessageText.setText(R.string.own_recipe_comment);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return userUpdatesList.size();
    }

    public static class UserUpdateHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.likerAvatar)
        CircleImageView ivAvatar;

        @BindView(R.id.tvMessageText)
        TextView tvMessageText;

        @BindView(R.id.ivFoodImage)
        ImageView ivFoodImage;

        @BindView(R.id.tvFoodName)
        TextView tvFoodName;

        public UserUpdateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindWithClickListener(UserUpdate userUpdate, OnMessageItemClickListener listener) {
            itemView.setOnClickListener(__ -> listener.onMessageClick(userUpdate));
        }
    }

    public interface OnMessageItemClickListener {

        void onMessageClick(UserUpdate update);
    }
}
