package com.blockchain.store.playmarket.utilities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.utilities.device.BuildUtils;

import java.io.File;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class MyPackageManager {

    public boolean isApplicationInstalled(String applicationPackage) {
        PackageManager packageManager = Application.getInstance().getPackageManager();
        try {
            packageManager.getPackageInfo(applicationPackage, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void installApkByFile(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (BuildUtils.shouldUseContentUri()) {
            intent.setDataAndType(FileProvider.getUriForFile(Application.getInstance().getApplicationContext(), "com.blockchain.store.playmarket.fileprovider", file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + file), "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Application.getInstance().getApplicationContext().startActivity(intent);
    }

    public void openAppByPackage(String applicationPackage) {
        if (isApplicationInstalled(applicationPackage)) {
            Intent LaunchIntent = Application.getInstance().getApplicationContext().getPackageManager().getLaunchIntentForPackage(applicationPackage);
            Application.getInstance().getApplicationContext().startActivity(LaunchIntent);
        }
    }
}
