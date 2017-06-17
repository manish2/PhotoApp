package com.example.manish.photoapp.Logic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manish.photoapp.Activities.MainActivity;
import com.example.manish.photoapp.Contracts.CustomDialog;
import com.example.manish.photoapp.Database.FeedReaderDbHelper;
import com.example.manish.photoapp.Logic.DateFunctions;
import com.example.manish.photoapp.Models.DateTaggedPhoto;
import com.example.manish.photoapp.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by manish on 2017-05-21.
 */

public class DateDialog implements CustomDialog {
   private Context _caller;
   private FeedReaderDbHelper _dbHelper;
   private String _photoPath;
   public DateDialog(Context caller, String photoPath) {
       _caller = caller;
       _photoPath = photoPath;
   }
   public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.date_dialog);
        Button okButton = (Button)dialog.findViewById(R.id.ok_button);
        Button cancelButton = (Button)dialog.findViewById(R.id.cancel_button);
        final EditText currDate = (EditText)dialog.findViewById(R.id.date_field);
        final String currentDate = DateFunctions.getInstance().getCurrentDate();
        currDate.setText(DateFunctions.getInstance().getCurrentDate());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Calendar inputDate = DateFunctions.getInstance().convertFromStringToCalendar(currDate.getText().toString());
                    Calendar today = DateFunctions.getInstance().convertFromStringToCalendar(currentDate);
                    boolean dateIsCorrect = DateFunctions.getInstance().isValidDate(inputDate,today);
                    if(!dateIsCorrect) {
                        Toast.makeText(dialog.getContext(), "Failed to save! Date cannot be greater than current day", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String saveableDate = currDate.getText().toString();
                        DateTaggedPhoto photoToSave = new DateTaggedPhoto(_photoPath, saveableDate);
                        saveEntryToDB(dialog,photoToSave);
                    }
                    dialog.dismiss();
                    Intent i = new Intent(_caller, MainActivity.class);
                    _caller.startActivity(i);
                } catch (ParseException e) {
                    Toast.makeText(dialog.getContext(), "Failed to save! Error occurred while processing date! Must be in format YYYY-MM-DD", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
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
    private void saveEntryToDB(Dialog dialog, DateTaggedPhoto photo) {
        _dbHelper = new FeedReaderDbHelper(dialog.getContext());
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        if(_dbHelper.insertEntryByDate(photo, db)) {
            Toast.makeText(dialog.getContext(), "Successfully saved photo",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(dialog.getContext(), "Unable to save photo, please try again",Toast.LENGTH_SHORT).show();
        }
    }
}
