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
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = true;
    static String[] color = new String[]{
        "White", "Yellow", "Green", "Cyan", "Blue",
        "Magenta", "Red", "Light Gray", "Gray", "Dark Gray"
    };
    static int colorNumber;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    static String metrics = "";
    static AlarmManager am;
    static PendingIntent alarmIntent;

    protected static int checkDelay = 5000;

    protected PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);
        Intent serviceIntent = new Intent(this, BatteryMonitorService.class);
        alarmIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
        serviceIntent.putExtra("alarmIntent",alarmIntent);
        serviceIntent.putExtra("pIntent",pIntent);

        //Layout elements here
        Spinner spinner = (Spinner) findViewById(R.id.colorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colors_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(alarmIntent != null && am != null){
            am.cancel(alarmIntent);
        }//if

        am = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
        am.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                checkDelay,
                alarmIntent);

    }//OnCreate

    @Override
    protected void onResume(){
        super.onResume();
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
                .setLights(Color.MAGENTA, 1000, 1000)
                .build();

        NotificationManager manager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }//batteryFull

}//MainActivity
