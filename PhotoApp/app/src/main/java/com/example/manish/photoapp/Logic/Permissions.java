package com.example.manish.photoapp.Logic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by manish on 2017-05-20.
 */

public class Permissions {
    private Context _caller;
    public Permissions(Context caller) {
        _caller = caller;
    }
    public boolean writePermissionGranted() {
        int result = ContextCompat.checkSelfPermission(_caller, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
    public boolean cameraPermissionGranted() {
        int result = ContextCompat.checkSelfPermission(_caller, android.Manifest.permission.CAMERA);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
}
