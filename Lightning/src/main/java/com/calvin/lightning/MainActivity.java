package com.calvin.lightning;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {

    static boolean full = true;
    static long[] vibrate = new long[]{100, 500, 500, 500};
    static double fullModifier = 1.0;
    static String metrics = "";
    static AlarmManager am;
    static PendingIntent alarmIntent;

    static boolean alarming = true;
    static boolean vibrating = true;
    static boolean lightsOn = true;
    static int[] colorList = {
        Color.MAGENTA,
        Color.WHITE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.RED,
        Color.LTGRAY,
        Color.GRAY,
        Color.DKGRAY
    };
    static int color = Color.MAGENTA;
    static int fullLevel = 100;

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

        if(alarmIntent != null && am != null){
            am.cancel(alarmIntent);
        }//if

        am = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
        am.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                checkDelay,
                alarmIntent);

        Switch alarmSwitch = (Switch) findViewById(R.id.alarmSwitch);
        alarmSwitch.setChecked(alarming);
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                alarming = on;
        }});//setOnCheckedChangeListener

        CheckBox vibrateCheck = (CheckBox) findViewById(R.id.vibrateCheck);
        vibrateCheck.setChecked(vibrating);
        vibrateCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                vibrating = on;
        }});//setOnCheckedChangeListener

        CheckBox LEDCheck = (CheckBox) findViewById(R.id.LEDCheck);
        LEDCheck.setChecked(lightsOn);
        LEDCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                lightsOn = on;
        }});//setOnCheckedChangeListener

        Spinner colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        colorSpinner.setPrompt("Choose a Color");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.colors_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = colorList[position];
            }//onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }//onNothingSelected
        });//setOnItemSelectedListener

        SeekBar full = (SeekBar) findViewById(R.id.full);
        final TextView fullNumber = (TextView) findViewById(R.id.fullNumber);
        fullNumber.setText(String.valueOf(fullLevel));
        full.setProgress(fullLevel);
        full.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fullLevel = progress;
                fullModifier = (double)progress / seekBar.getMax();
                fullNumber.setText(String.valueOf(progress));
            }//onProgressChanged

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing
            }//onStartTrackingTouch
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //do nothing
            }//onStopTrackingTouch
        });//setOnSeekBarChangeListener
    }//OnCreate

    @Override
    protected void onResume(){
        super.onResume();
    }//onResume

    static void batteryFull(PendingIntent pIntent, Context context){
        MainActivity.full = true;
        if(!alarming) return;

        long[] vibrate = new long[] {0, 0, 0, 0};
        if(vibrating)
            vibrate = MainActivity.vibrate;

        int color = Color.TRANSPARENT;
        if(lightsOn)
            color = MainActivity.color;

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Your battery is full!")
                .setContentText("Unplug to save power.")
                .setContentIntent(pIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(vibrate)
                .setLights(color, 1000, 1000)
                .build();

        NotificationManager manager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }//batteryFull

}//MainActivity
