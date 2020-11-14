package com.tectro.mobileapp6.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.tectro.mobileapp6.R;

public class HeroEndFightDialog extends DialogFragment {
    private Activity context;
    private String message;
    private Runnable okFunction;

    public HeroEndFightDialog(Activity context, String message, Runnable okFunction) {
        this.context = context;
        this.message = message;
        this.okFunction = okFunction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setMessage(message)
                .setPositiveButton("ОК", (dialog, id) -> okFunction.run());

        return builder.create();
    }
}
