package com.example.startcountdown;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service {
    private final static String TAG = TimerService.class.getSimpleName();

    public static final String COUNTDOWN_BR = "com.example.startcountdown";
    Intent bi = new Intent(COUNTDOWN_BR);
    CountDownTimer cuentaRegresiva = null;

    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager preferencesManager = new SharedPreferencesManager(this);
        cuentaRegresiva = new CountDownTimer(420000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long segundos = millisUntilFinished / 1000;
                bi.putExtra("countdown", segundos);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    preferencesManager.setTimeFinished(Math.toIntExact(segundos));
                }
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
                Log.i("Finished", "");
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        cuentaRegresiva.cancel();
        super.onDestroy();
    }

}
