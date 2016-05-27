package com.dhvtsu.pampangafloodwatch.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mleano on 5/24/2016.
 */
public class EZSharedPreferences {

    private static String TAG = EZSharedPreferences.class.getSimpleName();

    private static final String USER_PREFERENCES = "RigPirate_Prefs";

    public static final String KEY_LOGIN = "Login";
    public static final String KEY_GATEWAY = "SmsGateway";

    public static SharedPreferences getSharedPref(Context ctx) {
        return ctx.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }

    // = = = = = = = = = = = = = = = = = = = = = = =
    // GETTER
    // = = = = = = = = = = = = = = = = = = = = = = =
    public static int getLogin(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_LOGIN, 0);
    }

    public static String getGateway(Context ctx) {
        return getSharedPref(ctx).getString(KEY_GATEWAY, null);
    }

    // = = = = = = = = = = = = = = = = = = = = = = =
    // SETTER
    // = = = = = = = = = = = = = = = = = = = = = = =
    public static void setLogin(Context ctx, int i) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LOGIN, i);
        editor.commit();
    }

    public static void setGateway(Context ctx, String s) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString
                (KEY_GATEWAY, s);
        editor.commit();
    }
}
