package com.dhvtsu.pampangafloodwatch.fragment;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.builder.DialogBuilder;
import com.dhvtsu.pampangafloodwatch.data.DatabaseHelper;
import com.dhvtsu.pampangafloodwatch.data.EZSharedPreferences;
import com.dhvtsu.pampangafloodwatch.data.Flood;
import com.dhvtsu.pampangafloodwatch.data.Municipality;
import com.dhvtsu.pampangafloodwatch.service.SmsReceiver;
import com.dhvtsu.pampangafloodwatch.utils.LogUtil;
import com.dhvtsu.pampangafloodwatch.widgets.TouchImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    private static final String TAG = MapFragment.class.getSimpleName();
    private Context ctx = this.getActivity();

    @Bind(R.id.txtMap)
    TextView txtMap;
    @Bind(R.id.map)
    TouchImageView map;
    @Bind(R.id.tvGateway)
    TextView tvGateway;
    @Bind(R.id.tvBacolorWL)
    TextView tvBacolorWL;
    @Bind(R.id.tvFloridaWL)
    TextView tvFloridaWL;
    @Bind(R.id.tvLubaoWL)
    TextView tvLubaoWL;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cv;

    private String SMS_GATEWAY;

    private BroadcastReceiver mReceiver;

    private int mapDrawable[][][] = {
            {
                    {R.drawable.pampanga000, R.drawable.pampanga001, R.drawable.pampanga002, R.drawable.pampanga003},
                    {R.drawable.pampanga010, R.drawable.pampanga011, R.drawable.pampanga012, R.drawable.pampanga013},
                    {R.drawable.pampanga020, R.drawable.pampanga021, R.drawable.pampanga022, R.drawable.pampanga023},
                    {R.drawable.pampanga030, R.drawable.pampanga031, R.drawable.pampanga032, R.drawable.pampanga033},

            }, // {0 field}
            {
                    {R.drawable.pampanga100, R.drawable.pampanga101, R.drawable.pampanga102, R.drawable.pampanga103},
                    {R.drawable.pampanga110, R.drawable.pampanga111, R.drawable.pampanga112, R.drawable.pampanga113},
                    {R.drawable.pampanga120, R.drawable.pampanga121, R.drawable.pampanga122, R.drawable.pampanga123},
                    {R.drawable.pampanga130, R.drawable.pampanga131, R.drawable.pampanga132, R.drawable.pampanga133},
            }, // {1 field}
            {
                    {R.drawable.pampanga200, R.drawable.pampanga201, R.drawable.pampanga202, R.drawable.pampanga203},
                    {R.drawable.pampanga210, R.drawable.pampanga211, R.drawable.pampanga212, R.drawable.pampanga213},
                    {R.drawable.pampanga220, R.drawable.pampanga221, R.drawable.pampanga222, R.drawable.pampanga223},
                    {R.drawable.pampanga230, R.drawable.pampanga231, R.drawable.pampanga232, R.drawable.pampanga233},
            }, // {2 field}
            {
                    {R.drawable.pampanga300, R.drawable.pampanga301, R.drawable.pampanga302, R.drawable.pampanga303},
                    {R.drawable.pampanga310, R.drawable.pampanga311, R.drawable.pampanga312, R.drawable.pampanga313},
                    {R.drawable.pampanga320, R.drawable.pampanga321, R.drawable.pampanga322, R.drawable.pampanga323},
                    {R.drawable.pampanga330, R.drawable.pampanga331, R.drawable.pampanga332, R.drawable.pampanga333},
            }  // {3 field}
    };
    private int floodColorCode[] = {R.color.colorPrimary, R.color.floodLow, R.color.floodMed, R.color.floodHigh};
    private String floodStatus[] = {"Normal", "Low", "Med", "High"};
    private int mapCombination[] = {0, 0, 0};


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

        initData();

        return v;
    }

    private void initData() {
        setHasOptionsMenu(true);
        dbHelper = DatabaseHelper.getInstance(getActivity());
        initGateway();
        initSMSReceiver();
        initMap();
        readDB();
//        updateMap(1, 3);
//        updateMap(0, 1);
//        updateMap(2, 2);
    }

    private void initGateway() {
        SMS_GATEWAY = EZSharedPreferences.getGateway(getActivity());
        tvGateway.setText(SMS_GATEWAY);
        if (SMS_GATEWAY.equals(""))
            setGateway();


    }

    public void initSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter(SmsReceiver.SMS_RECEIVE_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                String message = extras.getString(SmsReceiver.SMS_MESSAGE);
                String sender = extras.getString(SmsReceiver.SMS_SENDER);

                onSMSReceived(sender, message);
//
            }
        };

        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    public void initMap() {
        mapCombination = EZSharedPreferences.getWaterLevel(getActivity());
        for (int ctr = 0; ctr < mapCombination.length; ctr++) {
            Log.d(TAG, "Value: " + mapCombination[ctr]);
            updateMap(ctr, mapCombination[ctr]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onSMSReceived(String sender, String message) {
        LogUtil.d(TAG, "Sender " + sender);
        LogUtil.d(TAG, "SMS GATEWAY " + SMS_GATEWAY);
        int waterLevel = -1;
        int area = -1;
        if (sender.equals(SMS_GATEWAY)) {
            LogUtil.d(TAG, "Message: " + message);

            if (message.contains(Municipality.MUN_BACOLOR))
                area = Municipality.BACOLOR;
            else if (message.contains(Municipality.MUN_FLORIDA))
                area = Municipality.FLORIDA;
            else if (message.contains(Municipality.MUN_LUBAO))
                area = Municipality.LUBAO;
            else
                area = -1;

            if (message.contains(Flood.RED)) {
                waterLevel = Flood.HIGH;
            } else if (message.contains(Flood.ORANGE)) {
                waterLevel = Flood.MID;
            } else if (message.contains(Flood.YELLOW)) {
                waterLevel = Flood.LOW;
            } else if (message.contains(Flood.NORMAL)) {
                waterLevel = Flood.NO_WATER;
            } else
                waterLevel = -1;

            if (area != -1 && waterLevel != -1) {
                updateMap(area, waterLevel);
                addToHistory(area, waterLevel);
            }
        }
    }

    private void addToHistory(int area, int waterLevel) {

        String time = getCurrentTime();
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();
        cv.put("area", area);
        cv.put("level", waterLevel);
        cv.put("timestamp", time);
        db.insert("tbl_history", null, cv);
        readDB();
    }

    private void readDB() {

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("tbl_history", null, "area = ?", new String[]{String.valueOf(1)}, null, null, null);
//        Cursor cursor = db.query("tbl_history", null, null, null, null, null, null);
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

    private void updateMap(int area, int waterLevel) {
        mapCombination[area] = waterLevel;
        map.setImageDrawable(getResources().getDrawable(mapDrawable[mapCombination[0]][mapCombination[1]][mapCombination[2]]));

        switch (area) {
            case Municipality.BACOLOR:
                if (waterLevel == Flood.LOW)
                    tvBacolorWL.setTextColor(Color.BLACK);
                else
                    tvBacolorWL.setTextColor(Color.WHITE);
                tvBacolorWL.setText(floodStatus[waterLevel]);

                tvBacolorWL.setBackgroundResource(floodColorCode[waterLevel]);
                break;

            case Municipality.FLORIDA:
                if (waterLevel == Flood.LOW)
                    tvFloridaWL.setTextColor(Color.BLACK);
                else
                    tvFloridaWL.setTextColor(Color.WHITE);
                tvFloridaWL.setText(floodStatus[waterLevel]);

                tvFloridaWL.setBackgroundResource(floodColorCode[waterLevel]);
                break;

            case Municipality.LUBAO:
                if (waterLevel == Flood.LOW)
                    tvLubaoWL.setTextColor(Color.BLACK);
                else
                    tvLubaoWL.setTextColor(Color.WHITE);
                tvLubaoWL.setText(floodStatus[waterLevel]);
                tvLubaoWL.setBackgroundResource(floodColorCode[waterLevel]);
                break;
        }
        EZSharedPreferences.setWaterLevel(getActivity(), mapCombination);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_fragment_map, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_gateway:
                setGateway();
                break;
        }

        return super.onOptionsItemSelected(item);


    }

    public void setGateway() {
        final EditText etGateway = new EditText(getActivity());
        etGateway.setInputType(InputType.TYPE_CLASS_PHONE);
        etGateway.setHint("+639XXXXXXXXXX");
        DialogBuilder.dialogBuilder(getActivity(), "SMS Gateway",
                etGateway, getString(R.string.done),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gatewayNumber = etGateway.getText().toString();
                        LogUtil.d(TAG, gatewayNumber);
                        EZSharedPreferences.setGateway(getActivity(), gatewayNumber);
                        SMS_GATEWAY = gatewayNumber;
                        tvGateway.setText(SMS_GATEWAY);
                        dialog.dismiss();
                    }
                }, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd", Locale.ENGLISH);
        String time = sdf.format(c.getTime());
        Log.d(TAG, time);

        return time;
    }
}
