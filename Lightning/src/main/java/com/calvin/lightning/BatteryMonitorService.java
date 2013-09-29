package com.calvin.lightning;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.SystemClock;

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

        batteryCheck();
    }//onHandleIntent

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

        return;
    }//batteryCheck

    class batteryTask extends TimerTask{

        @Override
        public void run() {
            batteryCheck();
        }
    }//batteryTask
}//BatteryMonitorService
