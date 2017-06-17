package com.example.manish.photoapp.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.manish.photoapp.R;

public class ImageEnlarger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enlarger);
        setFullSizedImage();
    }
    private void setFullSizedImage() {
        ImageView imageView = (ImageView)findViewById(R.id.full_image_viewer);
        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap)extras.getParcelable("Bitmap");
        if(bmp != null) {
            imageView.setImageBitmap(bmp);
        }
    }
}
