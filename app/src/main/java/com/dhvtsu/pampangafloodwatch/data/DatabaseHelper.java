package com.dhvtsu.pampangafloodwatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mleano on 5/17/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PFW_DB";
    public static final int DB_VERSION = 2;

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
//        createMunicipalities(db);
        createFloodHistory(db);
    }

    private void createFloodHistory(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_history(id integer primary key, area integer, level integer, timestamp varchar)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db, "tbl_history");
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db, String tblName) {
        db.execSQL("DROP TABLE IF EXISTS " + tblName);
    }

    public void createTable(SQLiteDatabase db, String tblName, ArrayList<Object> fields) {
        // String values =
        String tblFields = "(";
        for (int ctr = 0; ctr < fields.size(); ctr++) {
            if (fields.get(ctr) instanceof String) {
                tblFields += fields.get(ctr) + " varchar";
            }
            tblFields += ", ";
        }
        tblFields = tblFields.substring(0, tblFields.length() - 2) + ")";
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tblName + tblFields);
    }
}
