package com.example.manish.photoapp.Logic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.manish.photoapp.Contracts.CustomDialog;
import com.example.manish.photoapp.R;

/**
 * Created by manish on 2017-05-22.
 */

public class DeleteImageDialog implements CustomDialog {
    private Context _caller;
    public DeleteImageDialog(Context caller) {
        _caller = caller;
    }
    @Override
    public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.delete_dialog);
        Button deleteButton = (Button)dialog.findViewById(R.id.delete_image_button);
        Button cancelButton = (Button)dialog.findViewById(R.id.cancel_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
