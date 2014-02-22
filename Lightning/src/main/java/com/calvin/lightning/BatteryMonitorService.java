package com.calvin.lightning;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import java.util.Calendar;

public class BatteryMonitorService extends IntentService {

    private PendingIntent pIntent;
    private Intent battery;

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

        //TODO: remove metrics from final build
        Calendar cal = Calendar.getInstance();
        String time =
                cal.get(Calendar.HOUR_OF_DAY) +
                ":" + cal.get(Calendar.MINUTE) +
                ":" + cal.get(Calendar.SECOND);

        MainActivity.metrics =
                time + " " + level + " " + scale + " " + fullPower + " " + MainActivity.full;
        System.out.println(MainActivity.metrics);

        MainActivity.batteryFull(pIntent, this);
/*
        if(MainActivity.full && level < fullPower)
            MainActivity.full = false;

        else if(!MainActivity.full && level >= fullPower){
            MainActivity.batteryFull(pIntent, this);
        }//if
*/
    }//batteryCheck

}//BatteryMonitorService
