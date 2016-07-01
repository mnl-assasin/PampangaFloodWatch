package com.dhvtsu.pampangafloodwatch.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.builder.DialogBuilder;
import com.dhvtsu.pampangafloodwatch.data.EZSharedPreferences;
import com.dhvtsu.pampangafloodwatch.data.Flood;
import com.dhvtsu.pampangafloodwatch.data.Municipality;
import com.dhvtsu.pampangafloodwatch.service.SmsReceiver;
import com.dhvtsu.pampangafloodwatch.utils.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    @Bind(R.id.ivPampanga)
    ImageView ivPampanga;
    @Bind(R.id.txtMap)
    TextView txtMap;


    private static final String TAG = MapFragment.class.getSimpleName();
    private String SMS_GATEWAY;

    private Context ctx;

    private BroadcastReceiver mReceiver;


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

        SMS_GATEWAY = EZSharedPreferences.getGateway(getActivity());
        if (SMS_GATEWAY.equals("")) {
            LogUtil.d(TAG, "SMS Gateway not set!");
            setGateway();
        }

        txtMap.setText("Value " + mapCombination[0] + " : " + mapCombination[1] + " : " + mapCombination[2]);
//        txtMap.setText("87");
        initSMSReceiver();
        testing();
    }

    public void testing() {
        ArrayList<Object> fields = new ArrayList<>();

        fields.add("one");
        fields.add("two");
        fields.add("three");
        String tblFields = "(";
        for (int ctr = 0; ctr < fields.size(); ctr++) {
            if (fields.get(ctr) instanceof String) {
                tblFields += fields.get(ctr) + " varchar";
            }
            tblFields += ", ";
        }
        tblFields = tblFields.substring(0, tblFields.length() - 2) + ")";
        LogUtil.d(TAG, "DB FIELD " + tblFields);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    public void onSMSReceived(String sender, String message) {
        LogUtil.d(TAG, "Sender " + sender);
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
            }
        }
    }

    private void updateMap(int area, int waterLevel) {
        mapCombination[area] = waterLevel;
        txtMap.setText(txtMap.getText().toString() + "\nValue " + mapCombination[0] + " : " + mapCombination[1] + " : " + mapCombination[2]);

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
}
