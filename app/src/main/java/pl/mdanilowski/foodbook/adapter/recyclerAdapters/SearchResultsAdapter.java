package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.fragment.dashboard.SearchFragment;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.UserHolder> {

    @Inject
    FoodBookSimpleStorage foodBookSimpleStorage;

    private List<User> users = new ArrayList<>();
    private SearchFragment searchFragment;
    OnResultClickListener listener;

    public SearchResultsAdapter(SearchFragment fragment, OnResultClickListener listener) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.listener = listener;
        this.searchFragment = fragment;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public SearchResultsAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_query_result, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, listener);
        Glide.with(holder.itemView).load(user.getAvatarUrl()).into(holder.ivUser);
        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getEmail());
        holder.ibAddFriend.setVisibility(View.VISIBLE);
        holder.ivFriend.setVisibility(View.INVISIBLE);
        for (User follower : foodBookSimpleStorage.getUser().getFollowing()) {
            if (user.getUid().equals(follower.getUid())) {
                holder.ibAddFriend.setVisibility(View.INVISIBLE);
                holder.ivFriend.setVisibility(View.VISIBLE);
            }
        }
        holder.ibAddFriend.setOnClickListener(__ ->
                searchFragment.getFoodBookService().followUser(user, foodBookSimpleStorage.getUser())
                        .subscribe(documentReference -> {
                            holder.ibAddFriend.setVisibility(View.GONE);
                            holder.ivFriend.setVisibility(View.VISIBLE);
                            Toast.makeText(searchFragment.getContext(), "Added " + user.getName() + " to friends", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            Toast.makeText(searchFragment.getContext(), "Ups! An error occured", Toast.LENGTH_SHORT).show();
                            throwable.printStackTrace();
                        })
        );
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivUser)
        CircleImageView ivUser;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvEmail)
        TextView tvEmail;

        @BindView(R.id.ibAddFriend)
        ImageButton ibAddFriend;

        @BindView(R.id.ivFriend)
        ImageView ivFriend;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(User user, OnResultClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(user));
        }
    }

    public interface OnResultClickListener {
        void onItemClick(User user);
    }
}
