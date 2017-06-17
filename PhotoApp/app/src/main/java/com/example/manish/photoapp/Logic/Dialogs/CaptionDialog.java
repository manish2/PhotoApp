package com.example.manish.photoapp.Logic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manish.photoapp.Activities.MainActivity;
import com.example.manish.photoapp.Contracts.CustomDialog;
import com.example.manish.photoapp.Database.FeedReaderDbHelper;
import com.example.manish.photoapp.Models.CaptionTaggedPhoto;
import com.example.manish.photoapp.R;

/**
 * Created by manis on 2017-06-12.
 */

public class CaptionDialog implements CustomDialog {
    private Context _caller;
    private String _photoPath;
    public CaptionDialog(Context caller, String photoPath) {
        _caller = caller;
        _photoPath = photoPath;
    }
    @Override
    public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.caption_dialog);
        Button cancelButton = (Button)dialog.findViewById(R.id.caption_dialog_cancel);
        Button okButton = (Button)dialog.findViewById(R.id.caption_dialog_ok);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textToSave = (EditText)dialog.findViewById(R.id.caption_text);
                String textTag = textToSave.getText().toString();
                if(textTag != null && !textTag.matches("")) {
                    CaptionTaggedPhoto photo = new CaptionTaggedPhoto(_photoPath,textTag);
                    saveEntryToDB(textTag,dialog,photo);
                    dialog.dismiss();
                    Intent i = new Intent(_caller, MainActivity.class);
                    _caller.startActivity(i);
                }
                else {
                    Toast.makeText(dialog.getContext(), "A caption is required to save!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
    }
    private void saveEntryToDB(final String textTag, final Dialog dialog, CaptionTaggedPhoto photo) {
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(dialog.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(dbHelper.insertEntryByCaption(photo,db)) {
            Toast.makeText(dialog.getContext(), "Successfully saved photo",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(dialog.getContext(), "And error occured while saving your photo, please try again! ", Toast.LENGTH_LONG).show();
        }

    }
}
