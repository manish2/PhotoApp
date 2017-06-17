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
import com.example.manish.photoapp.Logic.DateFunctions;
import com.example.manish.photoapp.R;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by manish on 2017-05-21.
 */

public class FilterDateDialog implements CustomDialog {
    private Context _caller;
    private EditText _dateFromField;
    private EditText _dateToField;
    public FilterDateDialog(Context caller) {
        _caller = caller;
    }
    @Override
    public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.filter_date_dialog);
        Button cancelButton = (Button)dialog.findViewById(R.id.filer_dialog_cancel_button);
        Button okButton = (Button)dialog.findViewById(R.id.filter_dialog_ok_button);
        _dateFromField = (EditText)dialog.findViewById(R.id.date_from_field);
        _dateToField = (EditText)dialog.findViewById(R.id.date_to_field);
        prePopulateDateFrom(_dateFromField);
        prePopulateDateTo(_dateToField);
        setCancelButtonListener(cancelButton, dialog);
        setOkButtonListener(okButton, dialog);
        return dialog;
    }
    private void setOkButtonListener(final Button okButton, final Dialog dialog) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dF = _dateFromField.getText().toString();
                    String dT = _dateToField.getText().toString();
                    try {
                        final Calendar dateFrom = DateFunctions.getInstance().convertFromStringToCalendar(dF);
                        final Calendar dateTo = DateFunctions.getInstance().convertFromStringToCalendar(dT);
                        final DateFunctions df = DateFunctions.getInstance();
                        if(!df.isValidDate(dateFrom, dateTo)) {
                            Toast.makeText(dialog.getContext(), "Date From cannot be greater than Date To!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent i = new Intent(_caller, MainActivity.class);
                            String query = String.format("SELECT * FROM %s WHERE %s BETWEEN date('%s') AND date('%s')",IDataRecord.TABLE_NAME,IDataRecord.COLUMN_NAME_SUBTITLE,dF,dT);
                            i.putExtra("dateFilterQuery",query);
                            _caller.startActivity(i);
                        }
                        dialog.dismiss();
                    }
                    catch(Exception e) {
                        Toast.makeText(_caller, "Error occurred while processing date! Must be in format YYYY-MM-DD", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }
            });
    }
    private void setCancelButtonListener(final Button cancelButton, final Dialog dialog) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void prePopulateDateFrom(EditText dateTo) {
        String yesterday = DateFunctions.getInstance().getYesterday();
        dateTo.setText(yesterday);
    }
    private void prePopulateDateTo(EditText dateFrom) {
        String currDate = DateFunctions.getInstance().getCurrentDate();
        dateFrom.setText(currDate);
    }
}
