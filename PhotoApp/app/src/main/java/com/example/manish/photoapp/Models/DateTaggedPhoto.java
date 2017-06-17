package com.example.manish.photoapp.Models;

import com.example.manish.photoapp.Contracts.TaggedPhoto;

/**
 * Created by manish on 2017-05-21.
 */

public class DateTaggedPhoto implements TaggedPhoto {
    private String _photoPath;
    private String _date;
    public DateTaggedPhoto(String photoPath, String date) {
        _photoPath = photoPath;
        _date = date;
    }
    public String getPhotoPath() {
        return _photoPath;
    }
    public void setPhotoPath(String filePath) {
        _photoPath = filePath;
    }
    public String getDate() {
        return _date;
    }
    public void setDate(String date) {
        _date = date;
    }
}
