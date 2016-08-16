package com.dhvtsu.pampangafloodwatch.fragment;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.builder.DialogBuilder;
import com.dhvtsu.pampangafloodwatch.data.DatabaseHelper;
import com.dhvtsu.pampangafloodwatch.data.EZSharedPreferences;
import com.dhvtsu.pampangafloodwatch.data.Flood;
import com.dhvtsu.pampangafloodwatch.data.MapTiles;
import com.dhvtsu.pampangafloodwatch.data.Municipality;
import com.dhvtsu.pampangafloodwatch.service.SmsReceiver;
import com.dhvtsu.pampangafloodwatch.utils.LogUtil;
import com.dhvtsu.pampangafloodwatch.widgets.TouchImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MunicipalityFragment extends Fragment {

    public static final String TAG = MunicipalityFragment.class.getSimpleName();

    @Bind(R.id.map)
    TouchImageView map;
    @Bind(R.id.txtMap)
    TextView txtMap;
    @Bind(R.id.tvArea)
    TextView tvArea;
    @Bind(R.id.tvGateway)
    TextView tvGateway;
    @Bind(R.id.tvArea1)
    TextView tvArea1;
    @Bind(R.id.tvArea1Status)
    TextView tvArea1Status;
    @Bind(R.id.tvArea2)
    TextView tvArea2;
    @Bind(R.id.tvArea2Status)
    TextView tvArea2Status;
    @Bind(R.id.tvArea3)
    TextView tvArea3;
    @Bind(R.id.tvArea3Status)
    TextView tvArea3Status;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cv;

    String[] municipalities = {"Bacolor", "Florida Blanca", "Lubao"};
    String[] all_bacolor = {"All Bacolor", "Cabalantian", "Cabetican", "San Vicente"};
    String[] all_florida = {"All Florida", "Paguiruan", "Poblacion", "Solib"};
    String[] all_lubao = {"All Lubao", "San Isidro", "San Roque", "Sta. Cruz"};

    String[][] area = {all_bacolor, all_florida, all_lubao};

    private String SMS_GATEWAY;
    private BroadcastReceiver mReceiver;

    private int floodColorCode[] = {R.color.colorPrimary, R.color.floodLow, R.color.floodMed, R.color.floodHigh};
    private String floodStatus[] = {"Normal", "Low", "Med", "High"};

    int lastArea;

    public MunicipalityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_municipality, container, false);
        ButterKnife.bind(this, v);


        //TEST DATA
//        int x[] = {1, 1, 2};
//        EZSharedPreferences.setBacolorLevel(getActivity(), x);
//        int y[] = {2, 2, 3};
//        EZSharedPreferences.setFLoridaLevel(getActivity(), y);
//        int z[] = {0, 3, 3};
//        EZSharedPreferences.setLubaoLevel(getActivity(), z);
        initData();

        return v;
    }

    private void initData() {
        setHasOptionsMenu(true);
        dbHelper = DatabaseHelper.getInstance(getActivity());
        initGateway();
        initSMSReceiver();
        initMap();

    }

    private void initGateway() {
        SMS_GATEWAY = EZSharedPreferences.getGateway(getActivity());
        tvGateway.setText(SMS_GATEWAY);
        tvGateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGateway();

            }
        });
        if (SMS_GATEWAY.equals(""))
            setGateway();
    }

    private void initSMSReceiver() {
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

    private void onSMSReceived(String sender, String message) {
        int waterLevel = -1;
        int area = -1;
        String key = "";
        Log.d(TAG, message);
        if (sender.equals(SMS_GATEWAY)) {
//        if (true) {
            //BACOLOR AREA
            if (message.contains(Municipality.BRGY_CABALANTIAN)) {
                area = Municipality.AREA_CABALANTIAN;
                key = EZSharedPreferences.BRGY_CABALANTIAN;

            } else if (message.contains(Municipality.BRGY_CABETICAN)) {
                area = Municipality.AREA_CABETICAN;
                key = EZSharedPreferences.BRGY_CABETICAN;

            } else if (message.contains(Municipality.BRGY_SAN_VICENTE)) {
                area = Municipality.AREA_SAN_VICENTE;
                key = EZSharedPreferences.BRGY_SAN_VICENTE;
            }
            //FLORIDA
            else if (message.contains(Municipality.BRGY_PAGUIRUAN)) {
                area = Municipality.AREA_PAGUIRUAN;
                key = EZSharedPreferences.BRGY_PAGUIRUAN;

            } else if (message.contains(Municipality.BRGY_POBLACION)) {
                area = Municipality.AREA_POBLACION;
                key = EZSharedPreferences.BRGY_POBLACION;

            } else if (message.contains(Municipality.BRGY_SOLIB)) {
                area = Municipality.AREA_SOLIB;
                key = EZSharedPreferences.BRGY_SOLIB;

            }
            // LUBAO
            else if (message.contains(Municipality.BRGY_SAN_ISIDRO)) {
                area = Municipality.AREA_SAN_ISIDRO;
                key = EZSharedPreferences.BRGY_SAN_ISIDRO;

            } else if (message.contains(Municipality.BRGY_SAN_ROQUE)) {
                area = Municipality.AREA_SAN_ROQUE;
                key = EZSharedPreferences.BRGY_SAN_ROQUE;

            } else if (message.contains(Municipality.BRGY_STA_CRUZ)) {
                area = Municipality.AREA_STA_CRUZ;
                key = EZSharedPreferences.BRGY_STA_CRUZ;
            }

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
                addDB(area, waterLevel);
//                updateWaterLevel(key, waterLevel);
                Log.d(TAG, "TEXT: " + key + " LEVEL: " + waterLevel);
                EZSharedPreferences.setWaterLevel(getActivity(), key, waterLevel);
                displayMap(lastArea);
            }
        }
    }

    private void initMap() {
        lastArea = EZSharedPreferences.getLastArea(getActivity());
        if (lastArea == -1) {
            setMapDisplay();
        } else {
            displayMap(lastArea);
        }
    }

    private void setMapDisplay() {

        final AlertDialog dialog;

        ListView lv = listBuilder(municipalities);

        dialog = dialogBuilder("Select Area to View", lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setArea(position);
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void setArea(final int areaIndex) {
        final AlertDialog dialog;
        ListView lv = listBuilder(area[areaIndex]);

        dialog = dialogBuilder("Select Area to View", lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setDisplay(areaIndex, position);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void setDisplay(int area, int position) {

        lastArea = area * 4 + position;
        Log.d(TAG, "Last Area: " + lastArea);
        EZSharedPreferences.setLastArea(getActivity(), lastArea);
        displayMap(lastArea);
    }

    private void displayMap(int lastArea) {
        modifyText(lastArea);
        if (lastArea % 4 == 0) {
            displayAllMap(lastArea);
        } else {
            displayIndividualMap(lastArea);
        }
    }

    private void modifyText(int lastArea) {
        int area = lastArea / 4;
        int position = lastArea % 4;
        Log.d(TAG, "LastArea: " + lastArea + " Area: " + area + " Position: " + position);
        tvArea.setText(this.area[area][position]);
//        int lastArea = area * 4 + position;
        if (lastArea % 4 != 0) {
            tvArea1.setVisibility(View.GONE);
            tvArea1Status.setVisibility(View.GONE);
            tvArea2.setVisibility(View.GONE);
            tvArea2Status.setVisibility(View.GONE);
            tvArea3.setVisibility(View.GONE);
            tvArea3Status.setVisibility(View.GONE);
        } else {
            tvArea1.setVisibility(View.VISIBLE);
            tvArea1Status.setVisibility(View.VISIBLE);
            tvArea2.setVisibility(View.VISIBLE);
            tvArea2Status.setVisibility(View.VISIBLE);
            tvArea3.setVisibility(View.VISIBLE);
            tvArea3Status.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 0 - Bacolor
     * 1 - Florida Blanca
     * 2 - Lubao
     */
    private void displayAllMap(int lastArea) {
        int area = lastArea / 4;
        Log.d(TAG, "Display All Map area: " + area);

        switch (area) {
            case 0: // Bacolor
                tvArea1.setText(Municipality.BRGY_CABALANTIAN);
                tvArea2.setText(Municipality.BRGY_CABETICAN);
                tvArea3.setText(Municipality.BRGY_SAN_VICENTE);
                break;
            case 1: // Florida Blanca
                tvArea1.setText(Municipality.BRGY_PAGUIRUAN);
                tvArea2.setText(Municipality.BRGY_POBLACION);
                tvArea3.setText(Municipality.BRGY_SOLIB);
                break;
            case 2: // Lubao
                tvArea1.setText(Municipality.BRGY_SAN_ISIDRO);
                tvArea2.setText(Municipality.BRGY_SAN_ROQUE);
                tvArea3.setText(Municipality.BRGY_STA_CRUZ);
                break;
        }
        updateAllMap(area);
    }

    private void updateAllMap(int area) {
        int brgyLevel[] = new int[3];
        int resId = -1;
        switch (area) {
            case 0:
                brgyLevel = EZSharedPreferences.getBacolorLevel(getActivity());
                resId = MapTiles.allBacolor[brgyLevel[0]][brgyLevel[1]][brgyLevel[2]];
                break;
            case 1:
                brgyLevel = EZSharedPreferences.getFloridaLevel(getActivity());
                resId = MapTiles.allFlorida[brgyLevel[0]][brgyLevel[1]][brgyLevel[2]];
                break;
            case 2:
                brgyLevel = EZSharedPreferences.getLubaoLevel(getActivity());
                break;
        }

        if (brgyLevel != null) {
//            Log.d(TAG, "CABALANTIAN: " + brgyLevel[0] + " CABETICAN: " + brgyLevel[1] + " SAN VICENTE: " + brgyLevel[2]);
            tvArea1Status.setText(floodStatus[brgyLevel[0]]);
            tvArea2Status.setText(floodStatus[brgyLevel[1]]);
            tvArea3Status.setText(floodStatus[brgyLevel[2]]);

            tvArea1Status.setBackgroundResource(floodColorCode[brgyLevel[0]]);
            tvArea2Status.setBackgroundResource(floodColorCode[brgyLevel[1]]);
            tvArea3Status.setBackgroundResource(floodColorCode[brgyLevel[2]]);
        }

        if (resId != -1) {
            setDisplayMap(resId);
        }

    }

    private void setDisplayMap(int resId) {
        map.setImageResource(resId);
    }

    private void displayIndividualMap(int lastArea) {
        int area = lastArea;
        Log.d(TAG, "Display Individual Map area: " + area);
        int lastLevel = 0;
        int resId = -1;
        switch (area) {
            case Municipality.AREA_CABALANTIAN:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_CABALANTIAN);
                resId = MapTiles.cabalantian[lastLevel];
                break;
            case Municipality.AREA_CABETICAN:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_CABETICAN);
                resId = MapTiles.cabetican[lastLevel];
                break;
            case Municipality.AREA_SAN_VICENTE:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_SAN_VICENTE);
                resId = MapTiles.vicente[lastLevel];
                break;
            // ############
            case Municipality.AREA_PAGUIRUAN:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_PAGUIRUAN);
                resId = MapTiles.paguiruan[lastLevel];
                break;
            case Municipality.AREA_POBLACION:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_POBLACION);
                resId = MapTiles.poblacion[lastLevel];
                break;
            case Municipality.AREA_SOLIB:
                lastLevel = EZSharedPreferences.getWaterLevel(getActivity(), EZSharedPreferences.BRGY_SOLIB);
                resId = MapTiles.solib[lastLevel];
                break;
        }
        if (lastLevel != -1) {
            setDisplayMap(resId);
        }
    }


    private AlertDialog dialogBuilder(String title, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setView(view);

        return builder.create();

    }

    private ListView listBuilder(String[] stringArray) {
        ListView lv = new ListView(getActivity());
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArray);
        lv.setAdapter(adapter);

        return lv;
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


    // ############################
    // OPERATIONS STARTS HERE
    // ############################

    private void addDB(int area, int waterLevel) {
        String time = getCurrentTime();
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();
        cv.put("area", area);
        cv.put("level", waterLevel);
        cv.put("timestamp", time);
        db.insert("tbl_history", null, cv);
//        readDB();
    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd", Locale.ENGLISH);
        String time = sdf.format(c.getTime());
        Log.d(TAG, time);

        return time;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(mReceiver);
    }

    @OnClick(R.id.tvArea)
    public void onClick() {

        setMapDisplay();
    }
}
