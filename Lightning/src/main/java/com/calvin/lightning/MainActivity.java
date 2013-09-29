package com.calvin.lightning;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = false;
    static int color = Color.MAGENTA;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    static String metrics = "";
    static AlarmManager am;

    protected static int checkDelay = 5000;

    protected PendingIntent pIntent;
    protected PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);
        Intent serviceIntent = new Intent(this, BatteryMonitorService.class);
        serviceIntent.putExtra("pIntent",pIntent);
        serviceIntent.putExtra("alarmIntent",alarmIntent);
        alarmIntent = PendingIntent.getService(this, 0, serviceIntent, 0);

        Intent battery = this.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int charging = battery.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

        if(charging == 0){
            pluggedReceiver plugged = new pluggedReceiver();
            registerReceiver(plugged,
                    new IntentFilter(pluggedReceiver.filter));
        }//if

        else{
            pluggedReceiver.charging(this);
        }//else

    }//OnCreate

    @Override
    protected void onResume(){
        super.onResume();

        ((TextView) findViewById(R.id.scouter)).setText(metrics);
    }//onResume

    static void batteryFull(PendingIntent pIntent, Context context){
        MainActivity.full = true;
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Your battery is full!")
                .setContentText("Unplug to save power.")
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(MainActivity.vibrate)
                .setLights(MainActivity.color, 1000, 1000)
                .build();

        NotificationManager manager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }//batteryFull

}//MainActivity
