package com.calvin.lightning;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = true;
    static int color = Color.MAGENTA;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    protected static int checkDelay = 15000;
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

}//MainActivity
