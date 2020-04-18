package com.example.startcountdown;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "APP_SETTINGS";
    private  static final  String TIME_FINISHED  ="time_finished";


    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SharedPreferencesManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(APP_SETTINGS, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setTimeFinished(int time) {
        editor.putInt(TIME_FINISHED, time);
        editor.commit();
    }

    public int getTimeFinished() {

        return pref.getInt(TIME_FINISHED, 0);

    }


// other getters/setters
}