package com.calvin.lightning;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BatteryMonitorService extends IntentService {

    private PendingIntent pIntent;
    private Intent battery;
    private Timer timer;

    public BatteryMonitorService(){
        super("BatteryMonitorService");
    }//default constructor

    @Override
    protected void onHandleIntent(Intent workIntent) {

        pIntent = workIntent.getParcelableExtra("pIntent");

        timer = new Timer(true);
        timer.schedule(new batteryTask(), 0, MainActivity.checkDelay);


    }//onHandleIntent

    @Override
    public void onDestroy(){
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Service Killed!")
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(MainActivity.vibrate)
                .setLights(Color.YELLOW, 1000, 1000)
                .build();
        super.onDestroy();

        NotificationManager manager =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }//onDestroy
    void batteryCheck(){

        battery = this.registerReceiver(null,
            new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int  level= battery.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int  scale= battery.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        double fullPower = MainActivity.fullModifier * scale;

        Calendar cal = Calendar.getInstance();
        String time =
                cal.get(Calendar.HOUR_OF_DAY) +
                ":" + cal.get(Calendar.MINUTE) +
                ":" + cal.get(Calendar.SECOND);

        MainActivity.metrics =
                time + " " + level + " " + scale + " " + fullPower + " " + MainActivity.full;
        System.out.println(MainActivity.metrics);

        if(MainActivity.full && level < fullPower)
            MainActivity.full = false;

        if(!MainActivity.full && level >= fullPower){
            MainActivity.batteryFull(pIntent, this);
        }//if

        else{
            Notification notification = new Notification.Builder(this)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();
            NotificationManager manager =
                    (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, notification);
        }

        return;
    }//batteryCheck

    class batteryTask extends TimerTask{

        @Override
        public void run() {
            batteryCheck();
        }
    }//batteryTask
}//BatteryMonitorService
