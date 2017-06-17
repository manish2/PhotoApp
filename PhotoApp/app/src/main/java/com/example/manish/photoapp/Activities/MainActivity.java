package com.example.manish.photoapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.manish.photoapp.Contracts.IDataRecord;
import com.example.manish.photoapp.Database.FeedReaderDbHelper;
import com.example.manish.photoapp.Logic.CameraSettings;
import com.example.manish.photoapp.Logic.Dialogs.FilterOptionsDialog;
import com.example.manish.photoapp.Logic.ImagePath;
import com.example.manish.photoapp.Logic.Permissions;
import com.example.manish.photoapp.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private CameraSettings cameraSettings = CameraSettings.getInstance();
    private Intent _cameraIntent;
    private Permissions _permissions;
    private FeedReaderDbHelper _dbHelper;
    private SQLiteDatabase _db;
    private String _query = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _permissions = new Permissions(this);
        _dbHelper = new FeedReaderDbHelper(this);
        _db = _dbHelper.getReadableDatabase();
        _query = getIntent().getStringExtra("dateFilterQuery");
        if(_query == null) {
            _query = getIntent().getStringExtra("captionFilterQuery");
            if(_query == null) {
                _query = getIntent().getStringExtra("locationFilterQuery");
            }
        }
       // _db.execSQL("DELETE FROM Image");
        populateImages();
    }
    private void populateImages() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.gallery_pages);
        Cursor cursor = _db.rawQuery("SELECT * FROM Image ORDER BY " + IDataRecord.COLUMN_NAME_TITLE + " desc",null);
        if(_query != null) {
            cursor = _db.rawQuery(_query,null);
        }
        while(cursor.moveToNext()) {
            File imgFile = new  File(cursor.getString(1));
            if(imgFile.exists()) {
                final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                final ImageView image = new ImageView(MainActivity.this);
                image.setImageBitmap(myBitmap);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView imageView = (ImageView)findViewById(R.id.large_viewer);
                        imageView.setImageBitmap(myBitmap);
                    }
                });
                layout.addView(image);
            }
        }
        cursor.close();
    }
    public void openFilterOptionDialog(View v) {
        new FilterOptionsDialog(this).buildDialog().show();
    }
    /*
     * Opens the camera for image capture
     */
    public void openCamera(View v) {
        _cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //checks if device has camera and if it has permission to use camera
        if (cameraSettings.deviceHasCamera(this) && _permissions.cameraPermissionGranted()) {
            startActivityForResult(_cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
             Bundle extras = data.getExtras();
             Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(_permissions.writePermissionGranted()) {
                startTaggingIntent(getImageFilePath(imageBitmap));
            }
            else {
                Toast.makeText(getApplicationContext(), "Sorry could not save your image",Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
     * Saves the image taken by user to a file in DCIM
     */
    private String getImageFilePath(Bitmap savedPhoto) {
        ImagePath img = new ImagePath();
        Uri tempUri = img.getImageUri(getApplicationContext(), savedPhoto);
        return getRealPathFromURI(tempUri);
    }
    /*
     * Initializes and starts the tagging options intent
     */
    private void startTaggingIntent(String filePath) {
        Intent i = new Intent(this,Tagging.class);
        i.putExtra("filePath",filePath);
        startActivity(i);
    }
    /*
     * Returns the full path of the image
     */
    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public void enlargeImage(View v) {
        try {
            ImageView imageView = (ImageView)findViewById(R.id.large_viewer);
            if(imageView != null) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Intent i = new Intent(this,ImageEnlarger.class);
                Bundle extras = new Bundle();
                extras.putParcelable("Bitmap", bitmap);
                i.putExtras(extras);
                startActivity(i);
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occured! Please click on an image in the gallery to view in large", Toast.LENGTH_LONG).show();
        }
    }
}
