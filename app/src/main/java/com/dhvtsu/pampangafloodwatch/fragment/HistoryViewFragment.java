package com.dhvtsu.pampangafloodwatch.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.data.DatabaseHelper;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryViewFragment extends Fragment {
    private static String TAG = HistoryViewFragment.class.getSimpleName();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cv;


    public static HistoryViewFragment newInstance(int index) {
        HistoryViewFragment f = new HistoryViewFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public HistoryViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_view, container, false);
        int index = getArguments().getInt("index", 0);
        ButterKnife.bind(this, v);

        readDB(index);
        return v;
    }

    private void readDB(int index) {

        db = dbHelper.getReadableDatabase();


        Cursor cursor = db.query("tbl_history", null, "id = ?", new String[]{String.valueOf(index)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int area;
                int level;
                String time;

                area = cursor.getInt(1);
                level = cursor.getInt(2);
                time = cursor.getString(3);

                Log.d(TAG, area + " " + level + " " + time);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
