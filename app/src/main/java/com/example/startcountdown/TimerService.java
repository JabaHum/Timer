package com.example.startcountdown;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // startMyOwnForeground();
        }else{
            //startForeground(1, new Notification());
            }

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
        return START_REDELIVER_INTENT;
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


    private void startMyOwnForeground() {

        if (Build.VERSION.SDK_INT >= 26) {
            String NOTIFICATION_CHANNEL_ID = "com.example.startcountdown";
            String channelName = "Login Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            Intent notificationIntent  =  new Intent(this,MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.STARTFORGROUND_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent =   PendingIntent.getActivity(this,0,notificationIntent,0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("App is running in background")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(2, notification);
        }
    }
}
