package com.example.startcountdown;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    TextView timeView;
    private final static String TAG = MainActivity.class.getSimpleName();

    String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timeView = findViewById(R.id.timeView);

        //startMyOwnForeground();

        timeView.setOnClickListener(view -> {
          Intent i =   new Intent(this, TimerService.class);
          i.setAction(Constants.ACTION.STARTFORGROUND_ACTION);
            startService(i);
        });

    }


    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            startCounterDown(intent);

        }
    };

    public void startCounterDown(Intent intent) {
        if (intent.getExtras() != null) {
            long segundos  = intent.getLongExtra("countdown", 0);
            Log.i("segundos",""+segundos);
            segundos = getSegundos(segundos);
            startMyOwnForeground(segundos);

            if (segundos == 0){
                timeView.setText("Done");
            }
        }
    }

    private long getSegundos(long segundos) {
        long minutos;
        if (segundos >= 60) {
            minutos = segundos / 60;
            segundos = segundos - minutos * 60;
            if (minutos > 9) {
                if (segundos > 9) {



                    timeView.setText(String.valueOf(minutos) + ":" + String.valueOf(segundos));
                } else {
                    timeView.setText(String.valueOf(minutos) + ":0" + String.valueOf(segundos));
                }
            } else {
                if (segundos > 9) {
                    timeView.setText("0" + String.valueOf(minutos) + ":" + String.valueOf(segundos));
                } else {
                    timeView.setText("0" + String.valueOf(minutos) + ":0" + String.valueOf(segundos));
                }
            }
        } else {
            if (segundos > 9) {
                timeView.setText("00:" + String.valueOf(segundos));
            } else {
                timeView.setText("00:0" + String.valueOf(segundos));
            }

        }
        return segundos;
    }


    private void startMyOwnForeground(long m) {

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
                    .setContentText(timeView.getText())
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;

            manager.notify(2,notificationBuilder.build());
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));
        Log.i(TAG, "registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, TimerService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }
}
