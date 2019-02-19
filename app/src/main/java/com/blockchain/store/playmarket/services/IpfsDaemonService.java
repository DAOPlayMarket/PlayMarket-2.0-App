package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;

import androidx.annotation.Nullable;

public class IpfsDaemonService extends IntentService {
    private static final String TAG = "IpfsDaemonService";

    public IpfsDaemonService() {
        super("IpfsDaemonService");
    }

    @Override
    protected void onHandleIntent(@Nullable @android.support.annotation.Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent() called with: intent = [" + intent + "]");
        try {
            IPFSDaemon.run("daemon").waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
