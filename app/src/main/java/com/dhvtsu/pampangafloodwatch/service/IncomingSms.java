package com.dhvtsu.pampangafloodwatch.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {


    public static final String ACTION_RECEIVED_SMS = "com.ndevs.simplesms.ACTION_RECEIVED_SMS";
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    String smsBody;
    String smsAddress;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    smsBody = smsMessage.getMessageBody().toString();
                    smsAddress = smsMessage.getOriginatingAddress();

                    // Toast.makeText(context, smsAddress + " : " + smsBody, Toast.LENGTH_SHORT).show();
                } // end for loop

//            SplashScreen instance = SplashScreen.instance();
//                instance.receivedMessage(smsBody, smsAddress);
//                  MapFragment.newInstance(smsBody);
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }


}