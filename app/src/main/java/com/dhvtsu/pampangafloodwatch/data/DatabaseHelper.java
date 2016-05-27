package com.dhvtsu.pampangafloodwatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by mleano on 5/17/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "";
    public static final int DB_VERSION = 1;

    private static DatabaseHelper mInstance = null;

    private DatabaseHelper(Context mContext) {
        super(mContext, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }

        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db, String tblName) {
        db.execSQL("DROP TABLE IF EXISTS " + tblName);
    }

    public void createTable(String tblName, HashMap<String, String> fields) {
        // String values =
    }
}
