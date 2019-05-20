package com.blockchain.store.playmarket.referral;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import static com.blockchain.store.playmarket.PurchaseSDK.services.RemoteConstants.REMOTE_INTENT_NAME_REFERRAL;

public class ReferralManager extends JobIntentService {
    private static final String TAG = "PlayMarketReferral";

    public ReferralManager() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate() called");
        super.onCreate();

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
        Log.d(TAG, "start() called with: intent = [" + intent + "]");
        Intent mIntent  = getIntent();
        sendBroadcast(mIntent);
        stopForeground(true);
    }

    private Intent getIntent() {
        Log.d(TAG, "getIntent() called");
        Intent intent = new Intent(REMOTE_INTENT_NAME_REFERRAL);
        intent.putExtra("test", "Test");
        return intent;

    }
}
