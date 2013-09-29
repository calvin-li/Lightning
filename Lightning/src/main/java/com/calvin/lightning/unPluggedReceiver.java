package com.calvin.lightning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class unPluggedReceiver extends BroadcastReceiver{
    static String filter = Intent.ACTION_POWER_DISCONNECTED;

    @Override
    public void onReceive(Context context, Intent intent) {
        draining(context);
    }//onReceive

    static void draining(Context context){

        MainActivity.am.cancel(
                ( (MainActivity) context).alarmIntent);

        pluggedReceiver plugged = new pluggedReceiver();
        context.registerReceiver(plugged,
                new IntentFilter(pluggedReceiver.filter));

    }//draining

}//unPLuggedReceiver