package com.dhvtsu.pampangafloodwatch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dhvtsu.pampangafloodwatch.R;
import com.dhvtsu.pampangafloodwatch.builder.ToastBuilder;

public class MenuActivity extends AppCompatActivity {

    private static MenuActivity mInstance;

    public static MenuActivity getInstance() {
        return mInstance;
    }

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("get_msg");

                msg = msg.replace("\n", "");
                String body = msg.substring(msg.lastIndexOf(":") + 1, msg.length());
                String pNumber = msg.substring(0, msg.lastIndexOf(":"));

                ToastBuilder.shortToast(getApplicationContext(), "Number: " + pNumber + " : Message: " + body);
            }
        };

        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
