package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;
import com.koushikdutta.async.future.FutureCallback;
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

        File file;
        if (BuildUtils.shouldUseContentUri()) {
            File path = getApplicationContext().getFilesDir();
            file = new File(path, "app.apk");
        } else {
            file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "app.apk");
        }
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

    private void sendBroadCast(App app, int progress, Constants.DOWNLOAD_STATE state) { // implemented by callbacks on NotificationManager.class
        Intent intent = new Intent();
        intent.setAction(Constants.DOWNLOAD_SERVICE_ACTION_KEY);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_APP_ID_EXTRA, app.appId);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_PROGRESS_EXTRA, progress);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_STATE_EXTRA, state);
        sendBroadcast(intent);
    }
}
