package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.Constants;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = intent.getParcelableExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA);
        int intExtra = intent.getIntExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA, 0);
        Log.d(TAG, "onHandleIntent: " + intExtra);
        startForeground(intExtra, notification);
        return START_STICKY;
    }
}
