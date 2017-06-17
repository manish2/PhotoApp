package com.example.manish.photoapp.Logic;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by manish on 2017-05-07.
 */

public class CameraSettings {
    private static CameraSettings instance = null;
    //prevent instantiation
    private CameraSettings() {

    }
    public static CameraSettings getInstance() {
        if(instance == null) {
            instance = new CameraSettings();
        }
        return instance;
    }
    /**
     * Check if this device has a camera
     */
    public boolean deviceHasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            //device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public  void openCamera() {

    }
}
