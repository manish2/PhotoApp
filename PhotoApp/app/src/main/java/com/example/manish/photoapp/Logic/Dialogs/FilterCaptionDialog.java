package com.example.manish.photoapp.Logic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manish.photoapp.Activities.MainActivity;
import com.example.manish.photoapp.Contracts.CustomDialog;
import com.example.manish.photoapp.Contracts.IDataRecord;
import com.example.manish.photoapp.R;

/**
 * Created by manis on 2017-06-12.
 */

public class FilterCaptionDialog implements CustomDialog {
    private Context _caller;
    public FilterCaptionDialog(Context caller) {
        _caller = caller;
    }
    @Override
    public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.filter_caption_dialog);
        final EditText filterWord = (EditText)dialog.findViewById(R.id.keyword_text);
        Button okButton = (Button)dialog.findViewById(R.id.caption_filter_ok_button);
        Button cancelButton = (Button)dialog.findViewById(R.id.caption_filter_cancel_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyWord = filterWord.getText().toString();
                if(!keyWord.matches("")) {
                    String query = "SELECT * FROM " + IDataRecord.TABLE_NAME + " WHERE " + IDataRecord.COLUMN_NAME_SUBTITLE2 + " LIKE '%" +keyWord + "%'";
                    Intent i = new Intent(_caller, MainActivity.class);
                    i.putExtra("captionFilterQuery",query);
                    _caller.startActivity(i);
                }
                else {
                    Toast.makeText(dialog.getContext(), "Keyword cannot be empty!", Toast.LENGTH_SHORT).show();
                }
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
