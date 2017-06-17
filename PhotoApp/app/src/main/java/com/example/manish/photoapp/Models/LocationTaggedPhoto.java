package com.example.manish.photoapp.Models;

import com.example.manish.photoapp.Contracts.TaggedPhoto;

/**
 * Created by manish on 2017-05-22.
 */

public class LocationTaggedPhoto implements TaggedPhoto {
    private String _photoPath;
    private double _lat;
    private double _long;
    public LocationTaggedPhoto(String photoPath, double lat, double lon) {
        _photoPath = photoPath;
        _lat = lat;
        _long = lon;
    }
    @Override
    public String getPhotoPath() {
        return  _photoPath;
    }

    @Override
    public void setPhotoPath(String filePath) {
        _photoPath = filePath;
    }
    public double getLat()  {
        return _lat;
    }
    public double getLong() {
        return _long;
    }
    public void setLat(double lat) {
        _lat = lat;
    }
    public void setLong(double lon) {
        _long = lon;
    }
}
