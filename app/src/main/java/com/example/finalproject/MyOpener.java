package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * MyOpener class that extends SQLiteOpenHelper.
 * In this class we deal with the various database stuff.
 */
public class MyOpener extends SQLiteOpenHelper {

    // Creating variables for various database things so we can reference them later
    protected final static String DATABASE_NAME = "NasaEarthImageryDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "IMAGES";
    public final static String COL_DATE = "DATE";
    public final static String COL_LONGITUDE = "LONGITUDE";
    public final static String COL_LATITUDE = "LATITUDE";
    public final static String COL_IMAGE_NAME = "IMAGE_NAME";
    public final static String COL_ID = "_id";

    public MyOpener(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * onCreate method. If the database has yet to be creating this variable is called.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the SQL database with various columns and an auto incrementing key.
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DATE + " text,"
                + COL_LONGITUDE + " text,"
                + COL_LATITUDE + " text,"
                + COL_IMAGE_NAME + " text);");
    }


    /**
     * onUpgrade method. This method gets called if the database version on the device is
     * lower than VERSION_NUM.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Here we drop the old database.
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        // Here we create the new database.
        onCreate(db);
    }

    /**
     * onDowngrade method. This method gets called if the database version on the device is
     * higher than VERSION_NUM
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Here we drop the old database.
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        // Here we create the new database.
        onCreate(db);
    }
}