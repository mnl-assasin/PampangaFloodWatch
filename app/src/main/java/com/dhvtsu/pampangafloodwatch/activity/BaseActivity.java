package com.dhvtsu.pampangafloodwatch.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dhvtsu.pampangafloodwatch.data.DatabaseHelper;


/**
 * Created by mleano on 5/20/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DatabaseHelper.getInstance(BaseActivity.this);
    }

    public void startLoadingDialog(String message) {
        pDialog = new ProgressDialog(BaseActivity.this);
        pDialog.setMessage(message);
        pDialog.show();
    }

    public void stopLoadingDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//
//        DialogBuilder.dialogBuilder(BaseActivity.this, "Do you really want to exit?", getString(R.string.ok), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                BaseActivity.this.finish();
//            }
//        }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//    }

    protected abstract void initialized();

    protected abstract void initData();
}
