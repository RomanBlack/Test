package ru.romanblack.test.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import ru.romanblack.test.R;


public class ApplyDialog extends DialogFragment {

    public static ApplyDialog create() {
        ApplyDialog result = new ApplyDialog();

        return result;
    }

    private View okButton;
    private View cancelButton;

    private DialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DialogListener) {
            listener = (DialogListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_apply, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onDetach() {
        listener = null;

        super.onDetach();
    }

    private void initializeUi(View view) {
        okButton = view.findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClicked();
            }
        });

        cancelButton = view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    private void onOkClicked() {
        if (listener != null) {
            listener.ok();
        }

        dismiss();
    }

    private void onCancelClicked() {
        if (listener != null) {
            listener.cancel();
        }

        dismiss();
    }

    public interface DialogListener {
        public void ok();
        public void cancel();
    }
}
