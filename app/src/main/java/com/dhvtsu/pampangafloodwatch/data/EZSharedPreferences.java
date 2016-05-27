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

    public static SharedPreferences getSharedPref(Context ctx) {
        return ctx.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }

    // = = = = = = = = = = = = = = = = = = = = = = =
    // GETTER
    // = = = = = = = = = = = = = = = = = = = = = = =
    public static int getLogin(Context ctx) {
        return getSharedPref(ctx).getInt(KEY_LOGIN, 0);
    }

    // = = = = = = = = = = = = = = = = = = = = = = =
    // SETTER
    // = = = = = = = = = = = = = = = = = = = = = = =
    public static void setLogin(Context ctx, int i) {
        SharedPreferences.Editor editor = getSharedPref(ctx).edit();
        editor.putInt(KEY_LOGIN, i);
        editor.commit();
    }
}
