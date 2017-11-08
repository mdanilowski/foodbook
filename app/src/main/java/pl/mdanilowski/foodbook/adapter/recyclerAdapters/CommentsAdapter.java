package pl.mdanilowski.foodbook.adapter.recyclerAdapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.mdanilowski.foodbook.R;
import pl.mdanilowski.foodbook.model.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder> {

    List<Comment> comments = new ArrayList<>();
    OnAvatarClickListener onAvatarClickListener;

    public CommentsAdapter(OnAvatarClickListener onAvatarClickListener) {
        this.onAvatarClickListener = onAvatarClickListener;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void commentAdded(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }

    public void commentRemoved(int position) {
        comments.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        Glide.with(holder.itemView).load(comment.getAvatarUrl()).into(holder.ivAvatar);
        holder.tvName.setText(comment.getName());
        holder.tvCommentText.setText(comment.getCommentText());
        holder.ivAvatar.setOnClickListener(v -> onAvatarClickListener.onAvatarClick(comment.getUid()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvCommentText)
        TextView tvCommentText;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(String uid);
    }
}
