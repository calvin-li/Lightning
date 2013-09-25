package com.calvin.lightning;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = true;
    static int color = Color.MAGENTA;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    protected static int checkDelay = 10000;
    PendingIntent pIntent;
    TextView scouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);

        Intent serviceIntent = new Intent(this, BatteryMonitorService.class);
        serviceIntent.putExtra("pIntent",pIntent);
        this.startService(serviceIntent);

        scouter = (TextView)findViewById(R.id.scouter);
    }//OnCreate

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
