package com.example.startcountdown;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {
    private final static String TAG = TimerService.class.getSimpleName();

    public static final String COUNTDOWN_BR = "com.example.startcountdown";
    Intent bi = new Intent(COUNTDOWN_BR);
    CountDownTimer cuentaRegresiva = null;

    public int counter = 0;
    Context context;


    public TimerService(Context context) {
        this.context = context;
    }

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
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }



    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        cuentaRegresiva.cancel();
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        sendBroadcast(bi);
        stoptimertask();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sendBroadcast(bi);
    }
}
