package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.InstalledAppData;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.SharedPrefsUtil;
import com.koushikdutta.ion.Ion;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.security.MessageDigest;

import static com.blockchain.store.playmarket.utilities.Constants.BASE_URL;
import static com.blockchain.store.playmarket.utilities.Constants.DOWNLOAD_APP_URL;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class DownloadService extends IntentService {
    private static final int TIMEOUT_IN_MILLIS = 3000;
    private static final String TAG = "DownloadService";
    private int progress = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        App app = intent.getParcelableExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA);
        File file = new MyPackageManager().getFileFromApp(app);
        if (file.exists()) {
            file.delete();
        }
        InstalledAppData appData = new InstalledAppData();
        appData.setAppId(app.appId);
        appData.setNode(Hawk.get(BASE_URL));

        SharedPrefsUtil.addDownloadedApp(app.packageName, appData);
        NotificationManager.getManager().registerNewNotification(app);
        Ion.with(getBaseContext())
                .load(DOWNLOAD_APP_URL + app.appId)

                .setHeader("hash", getHashedAndroidId(getBaseContext()))
                .setTimeout(TIMEOUT_IN_MILLIS)
                .progress((downloaded, total) -> {
                    int tempProgress = (int) ((double) downloaded / total * 100);
                    if (tempProgress > progress) {
                        progress = tempProgress;
                        Log.d(TAG, "progress: downloaded: " + downloaded + ". Total: " + total + ". progress " + progress);
                        NotificationManager.getManager().updateProgress(app, progress);
                    }

                }).write(file).setCallback((exception, result) -> {
            if (exception == null) {
                NotificationManager.getManager().downloadCompleteWithoutError(app);
                if (Hawk.contains(Constants.SETTINGS_AUTOINSTALL_FLAG) && (boolean) Hawk.get(Constants.SETTINGS_AUTOINSTALL_FLAG)) {
                    installApk(result);
                }
            } else {
                new MyPackageManager().deleteLocalApkByPackageName(app.packageName, this);
                NotificationManager.getManager().downloadCompleteWithError(app, exception);
            }
        });

    }


    public static String getHashedAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return md5(androidId);
    }

    public static String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    private void installApk(File file) {
        new MyPackageManager().installApkByFile(file);
    }


}
