package com.dhvtsu.pampangafloodwatch.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mleano on 5/24/2016.
 */
public class EZSharedPreferences {

    private static String TAG = EZSharedPreferences.class.getSimpleName();

    private static final String USER_PREFERENCES = "PFW_Preferences";

    public static final String KEY_LOGIN = "Login";
    public static final String KEY_GATEWAY = "SmsGateway";
    public static final String KEY_LAST_BACOLOR = "last bacolor";
    public static final String KEY_LAST_FLORIDA = "last florida";
    public static final String KEY_LAST_LUBAO = "last lubao";

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
        return getSharedPref(ctx).getString(KEY_GATEWAY, "");
    }

    public static int[] getWaterLevel(Context ctx) {
        int map[] = new int[3];

        map[0] = getSharedPref(ctx).getInt(KEY_LAST_BACOLOR, 0);
        map[1] = getSharedPref(ctx).getInt(KEY_LAST_FLORIDA, 0);
        map[2] = getSharedPref(ctx).getInt(KEY_LAST_LUBAO, 0);
        return map;
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

    public static void setWaterLevel(Context ctx, int level[]) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LAST_BACOLOR, level[0]);
        editor.putInt(KEY_LAST_FLORIDA, level[1]);
        editor.putInt(KEY_LAST_LUBAO, level[2]);

        editor.commit();
    }
}
