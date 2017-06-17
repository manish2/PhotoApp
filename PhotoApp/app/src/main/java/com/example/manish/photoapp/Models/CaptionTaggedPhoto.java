package com.example.manish.photoapp.Models;

import com.example.manish.photoapp.Contracts.TaggedPhoto;

/**
 * Created by manis on 2017-06-12.
 */

public class CaptionTaggedPhoto implements TaggedPhoto {
    private String _photoPath;
    private String _caption;
    public CaptionTaggedPhoto(String photoPath, String cap) {
        _photoPath = photoPath;
        _caption = cap;
    }
    @Override
    public String getPhotoPath() {
       return _photoPath;
    }

    @Override
    public void setPhotoPath(String filePath) {
        _photoPath = filePath;
    }
    public String getCaption() {
        return _caption;
    }
    public void setCaption(String cap) {
        _caption = cap;
    }
}
