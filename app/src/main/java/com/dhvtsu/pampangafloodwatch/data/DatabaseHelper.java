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
        createMunicipalities(db);
    }

    private void createMunicipalities(SQLiteDatabase db) {

        ArrayList<Object> fields = new ArrayList<>();
        fields.add(Municipality.ID);
        fields.add(Municipality.NAME);
        createTable(db, Municipality.TBL_NAME, fields);

        db.execSQL("INSERT INTO " + Municipality.TBL_NAME + "(" + Municipality.ID + "," + Municipality.NAME + ") VALUES (" + Municipality.BACOLOR + ",'" + Municipality.MUN_BACOLOR + "')");
        db.execSQL("INSERT INTO " + Municipality.TBL_NAME + "(" + Municipality.ID + "," + Municipality.NAME + ") VALUES (" + Municipality.FLORIDA + ",'" + Municipality.MUN_FLORIDA + "')");
        db.execSQL("INSERT INTO " + Municipality.TBL_NAME + "(" + Municipality.ID + "," + Municipality.NAME + ") VALUES (" + Municipality.LUBAO + ",'" + Municipality.MUN_LUBAO + "')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db, Municipality.TBL_NAME);
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
