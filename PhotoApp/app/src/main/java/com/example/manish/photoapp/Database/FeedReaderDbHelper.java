package com.example.manish.photoapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.manish.photoapp.Contracts.IDataRecord;
import com.example.manish.photoapp.Models.CaptionTaggedPhoto;
import com.example.manish.photoapp.Models.DateTaggedPhoto;
import com.example.manish.photoapp.Models.LocationTaggedPhoto;

/**
 * Created by manish on 2017-05-20.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ImageData.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + IDataRecord.TABLE_NAME + " (" +
                    IDataRecord._ID + " INTEGER PRIMARY KEY," +
                    IDataRecord.COLUMN_NAME_TITLE + " TEXT," +
                    IDataRecord.COLUMN_NAME_SUBTITLE + " TEXT, " +
                    IDataRecord.COLUMN_NAME_SUBTITLE2 + " TEXT, " +
                    IDataRecord.COLUMN_NAME_SUBTITLE3 + " REAL, " +
                    IDataRecord.COLUMN_NAME_SUBTITLE4 + " REAL )" ;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertEntryByDate(DateTaggedPhoto entry, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(IDataRecord.COLUMN_NAME_TITLE, entry.getPhotoPath());
            values.put(IDataRecord.COLUMN_NAME_SUBTITLE, entry.getDate());
            db.insert(IDataRecord.TABLE_NAME, null, values);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
    public boolean insertEntryByLatLong(LocationTaggedPhoto entry, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(IDataRecord.COLUMN_NAME_TITLE, entry.getPhotoPath());
            values.put(IDataRecord.COLUMN_NAME_SUBTITLE3, entry.getLat());
            values.put(IDataRecord.COLUMN_NAME_SUBTITLE4, entry.getLong());
            db.insert(IDataRecord.TABLE_NAME, null, values);
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    public boolean insertEntryByCaption(CaptionTaggedPhoto entry, SQLiteDatabase db) {
        try {
            ContentValues values = new ContentValues();
            values.put(IDataRecord.COLUMN_NAME_TITLE, entry.getPhotoPath());
            values.put(IDataRecord.COLUMN_NAME_SUBTITLE2, entry.getCaption());
            db.insert(IDataRecord.TABLE_NAME, null, values);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

}
