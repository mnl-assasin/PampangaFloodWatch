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
    public static final String KEY_LAST_AREA = "last area";

    public static final String BRGY_CABALANTIAN = "Cabalantian";
    public static final String BRGY_CABETICAN = "Cabetican";
    public static final String BRGY_SAN_VICENTE = "San Vicente";

    public static final String BRGY_PAGUIRUAN = "Paguiruan";
    public static final String BRGY_POBLACION = "Poblacion";
    public static final String BRGY_SOLIB = "Solib";

    public static final String BRGY_SAN_ISIDRO = "San Isidro";
    public static final String BRGY_SAN_ROQUE = "San Roque";
    public static final String BRGY_STA_CRUZ = "Sta Cruz";


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

        map[0] = getSharedPref(ctx).getInt(BRGY_CABALANTIAN, 0);
        map[1] = getSharedPref(ctx).getInt(BRGY_CABETICAN, 0);
        map[2] = getSharedPref(ctx).getInt(BRGY_SAN_VICENTE, 0);
        return map;
    }

    public static int[] getBacolorLevel(Context ctx) {
        int map[] = new int[3];

        map[0] = getSharedPref(ctx).getInt(BRGY_CABALANTIAN, 0);
        map[1] = getSharedPref(ctx).getInt(BRGY_CABETICAN, 0);
        map[2] = getSharedPref(ctx).getInt(BRGY_SAN_VICENTE, 0);
        return map;
    }

    public static int[] getFloridaLevel(Context ctx) {
        int map[] = new int[3];

        map[0] = getSharedPref(ctx).getInt(BRGY_PAGUIRUAN, 0);
        map[1] = getSharedPref(ctx).getInt(BRGY_POBLACION, 0);
        map[2] = getSharedPref(ctx).getInt(BRGY_SOLIB, 0);
        return map;
    }

    public static int[] getLubaoLevel(Context ctx) {
        int map[] = new int[3];

        map[0] = getSharedPref(ctx).getInt(BRGY_SAN_ISIDRO, 0);
        map[1] = getSharedPref(ctx).getInt(BRGY_SAN_ROQUE, 0);
        map[2] = getSharedPref(ctx).getInt(BRGY_STA_CRUZ, 0);
        return map;
    }

    public static int getLastArea(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_LAST_AREA, -1);
    }

    // = = = = = = = = = = = = = = = = = = = = = = =
    // SETTER
    // = = = = = = = = = = = = = = = = = = = = = = =
    public static void setLogin(Context ctx, int i) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LOGIN, i);
        editor.apply();
    }

    public static void setGateway(Context ctx, String s) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putString
                (KEY_GATEWAY, s);
        editor.apply();
    }

    public static void setWaterLevel(Context ctx, int level[]) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LAST_BACOLOR, level[0]);
        editor.putInt(KEY_LAST_FLORIDA, level[1]);
        editor.putInt(KEY_LAST_LUBAO, level[2]);

        editor.apply();
    }

    public static void setBacolorLevel(Context ctx, int level[]) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(BRGY_CABALANTIAN, level[0]);
        editor.putInt(BRGY_CABETICAN, level[1]);
        editor.putInt(BRGY_SAN_VICENTE, level[2]);

        editor.apply();
    }

    public static void setFLoridaLevel(Context ctx, int level[]) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(BRGY_PAGUIRUAN, level[0]);
        editor.putInt(BRGY_POBLACION, level[1]);
        editor.putInt(BRGY_SOLIB, level[2]);

        editor.apply();
    }

    public static void setLubaoLevel(Context ctx, int level[]) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(BRGY_SAN_ISIDRO, level[0]);
        editor.putInt(BRGY_SAN_ROQUE, level[1]);
        editor.putInt(BRGY_STA_CRUZ, level[2]);

        editor.apply();
    }

    public static void setLastArea(Context ctx, int area) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LAST_AREA, area);
        editor.commit();
    }
}
