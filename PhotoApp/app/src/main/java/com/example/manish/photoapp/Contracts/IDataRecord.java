package com.example.manish.photoapp.Contracts;

import android.provider.BaseColumns;

/**
 * Created by manish on 2017-05-24.
 */

public interface IDataRecord extends BaseColumns {
    String TABLE_NAME = "Image";
    String COLUMN_NAME_TITLE = "FilePath";
    String COLUMN_NAME_SUBTITLE = "Date";
    String COLUMN_NAME_SUBTITLE2 = "Caption";
    String COLUMN_NAME_SUBTITLE3 = "Lat";
    String COLUMN_NAME_SUBTITLE4 = "Long";
}
