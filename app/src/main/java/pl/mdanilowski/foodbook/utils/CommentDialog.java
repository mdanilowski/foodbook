package pl.mdanilowski.foodbook.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pl.mdanilowski.foodbook.R;

public class CommentDialog extends DialogFragment {

    ButtonDialogClickListener listener;

    public static CommentDialog newInstance(ButtonDialogClickListener listener) {
        CommentDialog commentDialog = new CommentDialog();
        commentDialog.listener = listener;
        return commentDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View commentDialogLayout = getActivity().getLayoutInflater().inflate(R.layout.view_comment_dialog, null);
        builder.setView(commentDialogLayout);

        EditText etComment = commentDialogLayout.findViewById(R.id.etComment);
        Button btnComment = commentDialogLayout.findViewById(R.id.btnComment);
        Button btnCancel = commentDialogLayout.findViewById(R.id.btnCancel);

        btnComment.setOnClickListener(__ -> {
            dismiss();
            listener.onCommentClick(String.valueOf(etComment.getText()));
        });
        btnCancel.setOnClickListener(__ -> {
            dismiss();
            listener.onCancelClick();
        });
        return builder.create();
    }

    public interface ButtonDialogClickListener {
        void onCommentClick(String comment);
        void onCancelClick();
    }
}
