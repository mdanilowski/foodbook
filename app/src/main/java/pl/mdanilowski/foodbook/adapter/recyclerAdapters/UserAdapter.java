package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.app.App;
import pl.mdanilowski.foodbook.model.User;
import pl.mdanilowski.foodbook.service.FoodBookService;
import pl.mdanilowski.foodbook.utils.Storage.FoodBookSimpleStorage;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    @Inject
    FoodBookSimpleStorage foodBookSimpleStorage;

    @Inject
    FoodBookService foodBookService;

    private List<User> users = new ArrayList<>();

    OnResultClickListener listener;

    public UserAdapter(OnResultClickListener listener) {
        App.getApplicationInstance().getFoodbookAppComponent().inject(this);
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public UserAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
