package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.InstalledAppData;
import com.blockchain.store.playmarket.installer.SAIPackageInstaller;
import com.blockchain.store.playmarket.installer.rootless.RootlessSAIPackageInstaller;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.SharedPrefsUtil;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;

import static com.blockchain.store.playmarket.utilities.Constants.BASE_URL;
import static com.blockchain.store.playmarket.utilities.Constants.DOWNLOAD_APP_URL;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class DownloadService extends IntentService implements SAIPackageInstaller.InstallationStatusListener {
    private static final int TIMEOUT_IN_MILLIS = 30000;
    private static final String TAG = "DownloadService";
    private int progress = 0;
    private SAIPackageInstaller mInstaller;

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

        Builders.Any.B ionBuilder;
        if (IPFSDaemon.getIpfsProcess() != null && Hawk.get(Constants.IS_USE_IPFS_TO_DOWNLOAD, true)) {
            showToast("download via IPFS");
            String ipfsLoadUrl = "https://127.0.0.1:8080/ipfs/" + app.hash + "/" + app.files.apk;
            ionBuilder = Ion.with(getBaseContext())
                    .load(ipfsLoadUrl);
        } else {
            showToast("download as normal");
            ionBuilder = Ion.with(getBaseContext())
                    .load(DOWNLOAD_APP_URL + app.appId)
                    .setHeader("hash", getHashedAndroidId(getBaseContext()));
        }
        ionBuilder
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
        ArrayList<File> files = new ArrayList<File>();
        files.add(file);
        ensureInstallerActuality();
        mInstaller.startInstallationSession(mInstaller.createInstallationSession(files));
//        new MyPackageManager().installApkByFile(file);
    }

    private void ensureInstallerActuality() {
        SAIPackageInstaller actualInstaller = RootlessSAIPackageInstaller.getInstance(DownloadService.this);
        if (actualInstaller != mInstaller) {
            if (mInstaller != null)
                mInstaller.removeStatusListener(this);

            mInstaller = actualInstaller;
            mInstaller.addStatusListener(this);
//            mState.setValue(mInstaller.isInstallationInProgress() ? InstallerState.INSTALLING : InstallerState.IDLE);
        }
    }

    private void showToast(String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(DownloadService.this, text, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onStatusChanged(long installationID, SAIPackageInstaller.InstallationStatus status, @androidx.annotation.Nullable String packageNameOrErrorDescription) {

    }
}
