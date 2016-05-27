package com.dhvtsu.pampangafloodwatch.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.builder.ToastBuilder;
import com.dhvtsu.pampangafloodwatch.service.SmsReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    private BroadcastReceiver mReceiver;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        initData();
        return v;
    }

    private void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(SmsReceiver.SMS_RECEIVE_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                String message = extras.getString(SmsReceiver.SMS_MESSAGE);
                String sender = extras.getString(SmsReceiver.SMS_SENDER);

                ToastBuilder.shortToast(getActivity(), sender + "\n" + message);
            }
        };

        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
}
