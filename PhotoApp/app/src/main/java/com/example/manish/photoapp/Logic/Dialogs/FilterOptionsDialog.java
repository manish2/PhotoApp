package com.example.manish.photoapp.Logic.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.manish.photoapp.Activities.FilterLocation;
import com.example.manish.photoapp.Contracts.CustomDialog;
import com.example.manish.photoapp.R;

/**
 * Created by manish on 2017-05-24.
 */
public class FilterOptionsDialog implements CustomDialog {
    private Context _caller;
    public FilterOptionsDialog(Context caller) {
        _caller = caller;
    }
    @Override
    public Dialog buildDialog() {
        final Dialog dialog = new Dialog(_caller);
        dialog.setContentView(R.layout.filter_options_dialog);
        Button filterDateButton = (Button)dialog.findViewById(R.id.filter_by_date_button);
        Button filterCaptionButton = (Button)dialog.findViewById(R.id.filter_by_caption_button);
        Button filterLocationButton = (Button)dialog.findViewById(R.id.filter_by_location_button);
        Button cancelButton = (Button)dialog.findViewById(R.id.filter_options_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        filterDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterDateDialog(dialog.getContext()).buildDialog().show();
            }
        });
        filterCaptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FilterCaptionDialog(dialog.getContext()).buildDialog().show();
            }
        });
        filterLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context currContext= dialog.getContext();
                Intent i = new Intent(currContext, FilterLocation.class);
                currContext.startActivity(i);
            }
        });
        return dialog;
    }
}
