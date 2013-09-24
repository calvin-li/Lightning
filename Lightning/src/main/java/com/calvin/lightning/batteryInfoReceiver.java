package com.calvin.lightning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Handler;

public class batteryInfoReceiver extends BroadcastReceiver{
    protected static int checkDelay = 20000;

    @Override
    public void onReceive(Context context, Intent intent) {

        PendingIntent pIntent = intent.getParcelableExtra("pIntent");

        int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        double fullPower = MainActivity.fullModifier * scale;

        System.out.println(level + " " + scale + " " + fullPower + " " + MainActivity.full);

        if(MainActivity.full && level < fullPower)
            MainActivity.full = false;

        if(!MainActivity.full && level >= fullPower){
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
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);
        }//if

        final Handler handler = new Handler();
        final Context mContext = context;
        final Intent mIntent = intent;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                mContext.startService(mIntent);
            }
        }, checkDelay);


    }
}
