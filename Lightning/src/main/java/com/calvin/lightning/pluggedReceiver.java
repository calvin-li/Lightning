package com.calvin.lightning;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

public class pluggedReceiver extends BroadcastReceiver{
    static String filter = Intent.ACTION_POWER_CONNECTED;

    @Override
    public void onReceive(Context context, Intent intent) {
        charging(context);
    }//onReceive

    static void charging(Context context){

        MainActivity.am = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
        MainActivity.am.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                MainActivity.checkDelay,
                ((MainActivity)context).alarmIntent);

        unPluggedReceiver unplugged = new unPluggedReceiver();
        context.registerReceiver(unplugged,
                new IntentFilter(unPluggedReceiver.filter));

    }//charging

}//pluggedReceiver
