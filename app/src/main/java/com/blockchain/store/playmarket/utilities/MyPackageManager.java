package com.blockchain.store.playmarket.utilities;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class MyPackageManager {
    private static final String TAG = "MyPackageManager";

    public void startDownloadApkService(App app) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        Intent intent = new Intent(applicationContext, DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA, app);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA, app.getDownloadLink());
        applicationContext.startService(intent);
    }

    public boolean isApplicationInstalled(String applicationPackage) {
        PackageManager packageManager = Application.getInstance().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "Launch Activity :" + packageManager.getLaunchIntentForPackage(packageInfo.packageName));
        }
        try {
            packageManager.getPackageInfo(applicationPackage, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public File getFileFromApp(App app) {
        Context context = Application.getInstance().getApplicationContext();
        File file;
        if (BuildUtils.shouldUseContentUri()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(path, app.getFileName());
        } else {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), app.getFileName());
        }
        return file;
    }

    public boolean isAppFileExists(App app) {
        return getFileFromApp(app).exists();
    }

    public void uninstallApkByApp(App app) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + app.hash));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        applicationContext.startActivity(intent);
    }

    public void installApkByFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (BuildUtils.shouldUseContentUri()) {
            intent.setDataAndType(FileProvider.getUriForFile(Application.getInstance().getApplicationContext(),
                    "com.blockchain.store.playmarket.fileprovider", file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Application.getInstance().getApplicationContext().startActivity(intent);
    }

    public void installApkByApp(App app) {
        installApkByFile(getFileFromApp(app));
    }

    public void openAppByPackage(String applicationPackage) {
        Context applicationContext = Application.getInstance().getApplicationContext();

        if (isApplicationInstalled(applicationPackage)) {
            Intent intent = new Intent();
            intent.setPackage(applicationPackage);

            PackageManager pm = applicationContext.getPackageManager();
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

            if (resolveInfos.size() > 0) {
                ResolveInfo resolveInfo = resolveInfos.get(0);
                ActivityInfo activity = resolveInfo.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(name);

                applicationContext.startActivity(i);
            } else {
                // todo say error
            }
        }
    }
}
