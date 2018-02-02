package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.koushikdutta.ion.Ion;

import java.io.File;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        App app = intent.getParcelableExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA);
        String url = intent.getStringExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA);

        File file = new MyPackageManager().getFileFromApp(app);
        if (file.exists()) {
            file.delete();
        }
        NotificationManager.getManager().registerNewNotification(app);
        Ion.with(getBaseContext())
                .load(url)
                .progress((downloaded, total) -> {
                    int progress = (int) ((double) downloaded / total * 100);
                    Log.d(TAG, "progress: downloaded: " + downloaded + ". Total: " + total + ". progress " + progress);
                    NotificationManager.getManager().updateProgress(app, progress);
                }).write(file).setCallback((exception, result) -> {
            Log.d(TAG, "onCompleted() called with: e = [" + exception + "], result = [" + result + "]");
            if (exception == null) {
                NotificationManager.getManager().downloadCompleteWithoutError(app);
                installApk(result);
            } else {
                NotificationManager.getManager().downloadCompleteWithError(app);
            }
        });

    }

    private void installApk(File file) {
        new MyPackageManager().installApkByFile(file);
    }

}
