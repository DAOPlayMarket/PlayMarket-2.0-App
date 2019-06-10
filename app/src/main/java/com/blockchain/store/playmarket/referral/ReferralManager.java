package com.blockchain.store.playmarket.referral;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.blockchain.store.playmarket.data.entities.ReferralData;

import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.REMOTE_INTENT_NAME_REFERRAL;

public class ReferralManager extends JobIntentService {
    private static final String TAG = "PlayMarketReferral";

    public ReferralManager() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id_2", "Playmarket SDK channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id_2");

            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(1, notificationBuilder.build());
        }

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
        start(intent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        try {
            start(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_REDELIVER_INTENT;
    }
//
//    @Override
//    public IBinder onBind(@NonNull Intent intent) {
//        return null;
//    }

    private void start(Intent intent) {
        if(intent != null && intent.hasExtra("referralData")){
            ReferralData referralData = new com.blockchain.store.playmarket.utilities.ReferralManager().getReferralData(intent.getStringExtra("referralData"));
            Log.d(TAG, "start() called with: intent = [" + intent + "]");
            Intent mIntent  = getIntent(referralData);
            sendBroadcast(mIntent);
            stopForeground(true);
        } else {
            stopForeground(true);
        }
    }

    private Intent getIntent(ReferralData referralData) {
        Log.d(TAG, "getIntent() called");
        Intent intent = new Intent(REMOTE_INTENT_NAME_REFERRAL);
        intent.putExtra("referralData", referralData.referralCode);
        return intent;

    }
}
