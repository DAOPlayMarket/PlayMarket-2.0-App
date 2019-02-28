package com.blockchain.store.playmarket.utilities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class MyPackageManager {
    private static final String TAG = "MyPackageManager";

    public void startDownloadApkService(App app) {
        startDownloadApkService(app, false);
    }

    public void startDownloadApkService(App app, boolean isForceInstallFlag) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        Intent intent = new Intent(applicationContext, DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA, app);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA, app.getDownloadLink());
        intent.putExtra(Constants.DOWNLOAD_SERVICE_FORCE_INSTALL, isForceInstallFlag);
        applicationContext.startService(intent);
    }

    public boolean isApplicationInstalled(String applicationPackage) {
        PackageManager packageManager = Application.getInstance().getPackageManager();
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

    public File findFileByPackageName(String packageName, Context context) {
        File directory;
        if (BuildUtils.shouldUseContentUri()) {
            directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        } else {
            directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        }
        if (directory == null) {
            return null;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.getName().contains(packageName)) {
                return file;
            }
        }
        return null;
    }

    public int getVersionFromFile(File file) {
        if (file == null) return 0;
        String name = file.getName();
        if (name.contains(":")) {
            String version = name.substring(0, name.indexOf(":"));
            try {
                return Integer.parseInt(version);
            } catch (Exception e) {
                return 0;
            }

        } else {
            return 0;
        }
    }

    public boolean isAppFileExists(App app) {
        return getFileFromApp(app).exists();
    }

    public Constants.APP_STATE isHasUpdate(App app) {
        PackageInfo pinfo = null;
        Context applicationContext = Application.getInstance().getApplicationContext();
        try {
            pinfo = applicationContext.getPackageManager().getPackageInfo(app.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pinfo == null) {
            return Constants.APP_STATE.STATE_INSTALLED;
        }
        int appCode = 0;

        try {
            String number = app.version.replaceAll("\\D+", "");
            appCode = Integer.parseInt(number);
            File fileByPackageName = findFileByPackageName(app.packageName, applicationContext);
            int versionFromFile = getVersionFromFile(fileByPackageName);
            if (versionFromFile > pinfo.versionCode && versionFromFile <= appCode) {
                return Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appCode > pinfo.versionCode) {
            return Constants.APP_STATE.STATE_HAS_UPDATE;
        }
        return Constants.APP_STATE.STATE_INSTALLED;
    }

    public void uninstallApkByApp(App app) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + app.packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        applicationContext.startActivity(intent);
    }

    public void installApkByFile(File file) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (BuildUtils.shouldUseContentUri()) {
            intent.setDataAndType(FileProvider.getUriForFile(applicationContext,
                    applicationContext.getPackageName() + ".contentprovider", file), "application/vnd.android.package-archive");
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

            PackageManager pm = applicationContext.getPackageManager();
            Intent intent = new Intent(pm.getLaunchIntentForPackage(applicationPackage));
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (intent != null) {
                applicationContext.startActivity(intent);
            } else {
                // todo say error
            }
        }
    }

    public static List<ApplicationInfo> getAllInstalledApps() {
        Context applicationContext = Application.getInstance().getApplicationContext();
        PackageManager pm = applicationContext.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_SIGNATURES);
        Log.d(TAG, "getAllInstalledApps: " + packages);
        List<ApplicationInfo> installedApps = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo app : packages) {
            //checks for flags; if flagged, check if updated system app
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {
                installedApps.add(app);
            }
        }
        Log.d(TAG, "getAllInstalledApps: " + installedApps);
        return installedApps;
    }

    public static String prepareApplicationInfoForRequest() {
        List<ApplicationInfo> allInstalledApps = getAllInstalledApps();
        StringBuilder stringBuilder = new StringBuilder();
        for (ApplicationInfo allInstalledApp : allInstalledApps) {
            stringBuilder.append(allInstalledApp.packageName).append(",");
        }
        return stringBuilder.toString();
    }

    public static int getVersionNameByPackageName(String packageName) {
        Context applicationContext = Application.getInstance().getApplicationContext();
        try {
            PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static PackageManager get() {
        Context applicationContext = Application.getInstance().getApplicationContext();
        PackageManager pm = applicationContext.getPackageManager();
        return pm;
    }

    public static boolean isAppHasUpdate(App app) {
        try {
            int appVersionLocal = MyPackageManager.getVersionNameByPackageName(app.packageName);
            int appVersionWeb = Integer.parseInt(app.version);
            return appVersionWeb > appVersionLocal;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteLocalApkByPackageName(String packageName, Context context) {
        File file = findFileByPackageName(packageName, context);
        if (file != null && file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
}
