package com.calvin.lightning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    static boolean full = false;
    static int color = Color.WHITE;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double cap = 1.0;
    PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);
        //register battery broadcast receiver
        this.registerReceiver(this.batteryInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }//OnCreate

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            double fullPower = cap * scale;

            if(full && level < fullPower)
                full = false;

            if(!full && level >= fullPower){
                full = true;
                Notification notification = new Notification.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Your battery is full!")
                        .setContentIntent(pIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(vibrate)
                        .setLights(color, 1000, 1000)
                        .build();

                NotificationManager manager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0, notification);
            }//if
        }//onReceive
    };//BroadcastReceiver

}//MainActivity
