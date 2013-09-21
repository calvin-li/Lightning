package com.calvin.lightning;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

public class BatteryMonitorService extends IntentService {

    public BatteryMonitorService(){
        super("BatteryMonitorService");
    }//default constructor

    @Override
    protected void onHandleIntent(Intent workIntent) {

        this.registerReceiver(this.batteryInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }//onHandleIntent

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            double fullPower = MainActivity.cap * scale;

            System.out.println(level + " " + scale + " " + fullPower + " " + MainActivity.full);

            if(MainActivity.full && level < fullPower)
                MainActivity.full = false;

            if(!MainActivity.full && level >= fullPower){
                MainActivity.full = true;
                Notification notification = new Notification.Builder(BatteryMonitorService.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Your battery is full!")
                        .setContentIntent(MainActivity.pIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(MainActivity.vibrate)
                        .setLights(MainActivity.color, 1000, 1000)
                        .build();

                NotificationManager manager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(0, notification);
            }//if
        }//onReceive
    };//BroadcastReceiver

}//BatteryMonitorService
