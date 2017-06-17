package com.example.manish.photoapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.manish.photoapp.Logic.Dialogs.CaptionDialog;
import com.example.manish.photoapp.Logic.Dialogs.DateDialog;
import com.example.manish.photoapp.R;

import java.io.File;

public class Tagging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);
        setImage();
    }
    private void setImage() {
        String filePath = getIntent().getStringExtra("filePath");
        File imgFile = new File(filePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.large_picture);
            myImage.setImageBitmap(myBitmap);
        }
    }
    public void tagByDate(View v) {
        String filePath = getIntent().getStringExtra("filePath");
        new DateDialog(this,filePath).buildDialog().show();
    }
    public void tagByLocation(View v) {
        String filePath = getIntent().getStringExtra("filePath");
        Intent i = new Intent(this, LocationTag.class);
        i.putExtra("photoPath",filePath);
        startActivity(i);
    }
    public void tagByCaption(View v) {
        String filePath = getIntent().getStringExtra("filePath");
        new CaptionDialog(this,filePath).buildDialog().show();
    }
}
