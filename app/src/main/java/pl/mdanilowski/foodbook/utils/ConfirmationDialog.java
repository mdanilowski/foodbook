package pl.mdanilowski.foodbook.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import pl.mdanilowski.foodbook.R;

public class ConfirmationDialog extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    private ConfirmationDialog.OnClickResult onClickResult;

    public ConfirmationDialog() {
    }

    public static ConfirmationDialog newInstance(String title, String message, ConfirmationDialog.OnClickResult onClickResult) {
        ConfirmationDialog informationDialog = new ConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(MESSAGE, message);
        informationDialog.onClickResult = onClickResult;
        informationDialog.setArguments(bundle);
        return informationDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);
        String message = getArguments().getString(MESSAGE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            if (onClickResult != null)
                onClickResult.onPositiveClick();
            dialogInterface.dismiss();
        });
        dialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
            if (onClickResult != null) {
                onClickResult.onNegativeClick();
            }
            dialogInterface.dismiss();
        });
        return dialogBuilder.create();
    }

    public interface OnClickResult {

        void onPositiveClick();

        void onNegativeClick();
    }
}
