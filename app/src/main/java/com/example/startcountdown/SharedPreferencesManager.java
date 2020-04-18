package com.example.startcountdown;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";
    private  static final  String TIME_FINISHED  ="time_finished";

    // properties
    private static final String SOME_STRING_VALUE = "SOME_STRING_VALUE";
    private static final int PRIVATE_MODE = 0;
    // other properties...


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public SharedPreferencesManager(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getSomeStringValue(Context context) {
        return getSharedPreferences(context).getString(SOME_STRING_VALUE , null);
    }

    public static void setSomeStringValue(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(SOME_STRING_VALUE , newValue);
        editor.commit();
    }

    public static String getTimeFinished( Context context) {
        return getSharedPreferences(context).getString(TIME_FINISHED , null);
    }

    public static void setTimeFinished(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(TIME_FINISHED , newValue);
        editor.commit();
    }


// other getters/setters
}