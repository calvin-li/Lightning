package com.calvin.lightning;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = false;
    static int color = Color.MAGENTA;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    static String metrics = "";
    protected static int checkDelay = 10000;

    private AlarmManager am;
    private PendingIntent pIntent;
    private TextView scouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pIntent = PendingIntent.getActivity(this, 0, this.getIntent(), 0);

        pluggedReceiver plugged = new pluggedReceiver();
        registerReceiver(plugged,
                new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));

    }//OnCreate

    @Override
    protected void onResume(){
        super.onResume();

        scouter = (TextView)findViewById(R.id.scouter);
        scouter.setText(metrics);

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

    private class pluggedReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(context, BatteryMonitorService.class);
            serviceIntent.putExtra("pIntent",pIntent);
            PendingIntent alarmIntent = PendingIntent.getService(context, 0, serviceIntent, 0);

            am = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
            am.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    checkDelay,
                    alarmIntent);
        }//onReceive
    }//pluggedReceiver

}//MainActivity
