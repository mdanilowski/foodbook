package pl.mdanilowski.foodbook.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class InformationDialog extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String OK = "OK";
    private OnClickResult onClickResult;

    public InformationDialog() {
    }

    public static InformationDialog newInstance(String title, String message) {
        InformationDialog informationDialog = new InformationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(MESSAGE, message);
        informationDialog.setArguments(bundle);
        return informationDialog;
    }

    public static InformationDialog newInstance(String title, String message, OnClickResult onClickResult) {
        InformationDialog informationDialog = new InformationDialog();
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
        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(getActivity());
        dialoBuilder.setTitle(title);
        dialoBuilder.setMessage(message);
        dialoBuilder.setPositiveButton(OK, (dialogInterface, i) -> {
            if (onClickResult != null)
                onClickResult.onPositiveClick();
            dialogInterface.dismiss();
        });
        return dialoBuilder.create();
    }

    public interface OnClickResult {
        void onPositiveClick();
    }
}
