package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.blockchain.store.playmarket.Application;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    public static final String CUSTOM_INTENT = "com.text.my.intent";

    public static void setAlarm(boolean isForced) {
        Log.d(TAG, "setAlarm() called with: isForced = [" + isForced + "]");
        AlarmManager alarm = (AlarmManager) Application.getInstance().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        long when = System.currentTimeMillis() + (1000 * 60 * 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, when, getIntent());
        } else {
            alarm.setExact(AlarmManager.RTC_WAKEUP, when, getIntent());
        }
    }

    public static PendingIntent getIntent() {
        Intent intent = new Intent(Application.getInstance().getApplicationContext(), AlarmReceiver.class);
        intent.setAction(CUSTOM_INTENT);
        return PendingIntent.getBroadcast(Application.getInstance().getApplicationContext(), 111, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static void cancelAlarm() {
        AlarmManager alarm = (AlarmManager) Application.getInstance().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getIntent());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
        CheckForUpdateJobIntent.enqueueWork(context, CheckForUpdateJobIntent.class, 1001, intent);
    }


}
