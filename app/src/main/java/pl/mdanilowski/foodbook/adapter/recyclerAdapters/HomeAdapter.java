package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


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
import pl.mdanilowski.foodbook.model.userUpdates.MyFollowerLikes;
import pl.mdanilowski.foodbook.model.userUpdates.MyRecipeLike;
import pl.mdanilowski.foodbook.model.userUpdates.NewFollowersComment;
import pl.mdanilowski.foodbook.model.userUpdates.NewFollowersRecipe;
import pl.mdanilowski.foodbook.model.userUpdates.UserUpdatesBase;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.UserUpdateHolder> {

    private List<UserUpdatesBase> userUpdatesList = new ArrayList<>();
    private OnMessageItemClickListener listener;

    public HomeAdapter(OnMessageItemClickListener listener) {
        this.listener = listener;
    }

    public void addUpdate(UserUpdatesBase update) {
        userUpdatesList.add(0, update);
        notifyItemInserted(0);
        Collections.sort(userUpdatesList, (o1, o2) -> {
            if (o1.getAddDate() > o2.getAddDate()) return -1;
            else return 1;
        });
        notifyDataSetChanged();
    }

    public void removeFollowersLike(MyFollowerLikes like) {
        Iterator<UserUpdatesBase> iterator = userUpdatesList.iterator();
        while (iterator.hasNext()) {
            UserUpdatesBase tempObject = iterator.next();
            if (tempObject instanceof MyFollowerLikes) {
                MyFollowerLikes tempLike = (MyFollowerLikes) tempObject;
                if (like != null) {
                    if (tempLike.getLiker().getUid().equals(like.getLiker().getUid()) &&
                            tempLike.getRecipe().getRid().equals(like.getRecipe().getRid())) {
                        iterator.remove();
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void removeMyRecipeLike(MyRecipeLike myRecipeLike) {
        Iterator<UserUpdatesBase> iterator = userUpdatesList.iterator();
        while (iterator.hasNext()) {
            UserUpdatesBase tempObject = iterator.next();
            if (tempObject instanceof MyRecipeLike) {
                MyRecipeLike tempLike = (MyRecipeLike) tempObject;
                if (myRecipeLike != null) {
                    if (tempLike.getLiker().getUid().equals(myRecipeLike.getLiker().getUid()) &&
                            tempLike.getRecipe().getRid().equals(myRecipeLike.getRecipe().getRid())) {
                        iterator.remove();
                        notifyDataSetChanged();
                    }
                }
            }
        }
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
        switch (holder.getItemViewType()) {
            default:
                UserUpdatesBase userUpdate = userUpdatesList.get(position);
                holder.bindWithClickListener(userUpdate, listener);
                if (userUpdate instanceof MyRecipeLike) {
                    MyRecipeLike myRecipeLike = (MyRecipeLike) userUpdate;
                    UserUpdateHolder userUpdateHolder = (UserUpdateHolder) holder;
                    Glide.with(userUpdateHolder.itemView).load(myRecipeLike.getLiker().getAvatarUrl()).into(userUpdateHolder.ivAvatar);
                    userUpdateHolder.tvMessageText.setText(String.format("%s %s", myRecipeLike.getLiker().getName(), " lubi twoj przepis"));
                    if (!myRecipeLike.getRecipe().getPhotosUrls().isEmpty())
                        Glide.with(userUpdateHolder.itemView).load(myRecipeLike.getRecipe().getPhotosUrls().get(0)).into(userUpdateHolder.ivFoodImage);
                    userUpdateHolder.tvFoodName.setText(myRecipeLike.getRecipe().getName());
                }
                if (userUpdate instanceof NewFollowersComment) {
                    NewFollowersComment newFollowersComment = (NewFollowersComment) userUpdate;
                    UserUpdateHolder userUpdateHolder = (UserUpdateHolder) holder;
                    Glide.with(userUpdateHolder.itemView).load(newFollowersComment.getComment().getAvatarUrl()).into(userUpdateHolder.ivAvatar);
                    userUpdateHolder.tvMessageText.setText(String.format("%s %s %s", newFollowersComment.getComment().getName(), " skomentował przepis użytkownika ", newFollowersComment.getOwner().getName()));
                    if (!newFollowersComment.getRecipe().getPhotosUrls().isEmpty())
                        Glide.with(userUpdateHolder.itemView).load(newFollowersComment.getRecipe().getPhotosUrls().get(0)).into(userUpdateHolder.ivFoodImage);
                    userUpdateHolder.tvFoodName.setText(newFollowersComment.getRecipe().getName());
                }
                if (userUpdate instanceof NewFollowersRecipe) {
                    NewFollowersRecipe newFollowersRecipe = (NewFollowersRecipe) userUpdate;
                    UserUpdateHolder userUpdateHolder = (UserUpdateHolder) holder;
                    Glide.with(userUpdateHolder.itemView).load(newFollowersRecipe.getUser().getAvatarUrl()).into(userUpdateHolder.ivAvatar);
                    userUpdateHolder.tvMessageText.setText(String.format("%s %s", newFollowersRecipe.getUser().getName(), " dodał nowy przepis"));
                    if (newFollowersRecipe.getRecipe().getPhotosUrls() != null && !newFollowersRecipe.getRecipe().getPhotosUrls().isEmpty())
                        Glide.with(userUpdateHolder.itemView).load(newFollowersRecipe.getRecipe().getPhotosUrls().get(0)).into(userUpdateHolder.ivFoodImage);
                    userUpdateHolder.tvFoodName.setText(newFollowersRecipe.getRecipe().getName());
                }
                if (userUpdate instanceof MyFollowerLikes) {
                    MyFollowerLikes myFollowerLikes = (MyFollowerLikes) userUpdate;
                    UserUpdateHolder userUpdateHolder = (UserUpdateHolder) holder;
                    Glide.with(userUpdateHolder.itemView).load(myFollowerLikes.getLiker().getAvatarUrl()).into(userUpdateHolder.ivAvatar);
                    userUpdateHolder.tvMessageText.setText(String.format("%s %s", myFollowerLikes.getLiker().getName(), " polubił przepis"));
                    if (myFollowerLikes.getRecipe().getPhotosUrls() != null && !myFollowerLikes.getRecipe().getPhotosUrls().isEmpty())
                        Glide.with(userUpdateHolder.itemView).load(myFollowerLikes.getRecipe().getPhotosUrls().get(0)).into(userUpdateHolder.ivFoodImage);
                    userUpdateHolder.tvFoodName.setText(myFollowerLikes.getRecipe().getName());
                }
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

        public void bindWithClickListener(UserUpdatesBase userUpdatesBase, OnMessageItemClickListener listener){
            itemView.setOnClickListener(__ -> listener.onMessageClick(userUpdatesBase));
        }
    }

    public interface OnMessageItemClickListener {

        void onMessageClick(UserUpdatesBase update);
    }
}