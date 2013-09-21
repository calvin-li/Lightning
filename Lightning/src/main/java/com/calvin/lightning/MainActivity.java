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
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = true;
    static int color = Color.MAGENTA;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double cap = 1.0;
    static PendingIntent pIntent;
    TextView scouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);

        Intent serviceIntent = new Intent(this, BatteryMonitorService.class);
        this.startService(serviceIntent);


        scouter = (TextView)findViewById(R.id.scouter);
    }//OnCreate

}//MainActivity
