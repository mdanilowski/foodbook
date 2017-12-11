package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


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
import pl.mdanilowski.foodbook.model.User;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private List<User> followingUsers = new ArrayList<>();
    OnFollowerClickListener listener;

    public FollowAdapter(OnFollowerClickListener listener) {
        this.listener = listener;
    }

    public void setFollowingUsers(List<User> followingUsers) {
        this.followingUsers = followingUsers;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_following_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = followingUsers.get(position);
        holder.bind(user, listener);
        if(user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()){
            Glide.with(holder.itemView).load(user.getAvatarUrl()).into(holder.ivUser);
        }
        holder.tvEmail.setText(user.getEmail());
        holder.tvName.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return followingUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUser)
        ImageView ivUser;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvEmail)
        TextView tvEmail;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User user, OnFollowerClickListener listener){
            itemView.setOnClickListener(v -> listener.onUserClick(user));
        }
    }

    public interface OnFollowerClickListener {
        void onUserClick(User user);
    }
}
