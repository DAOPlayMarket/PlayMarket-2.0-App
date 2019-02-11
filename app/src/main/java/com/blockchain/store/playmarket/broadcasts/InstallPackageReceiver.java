package com.blockchain.store.playmarket.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.InstalledAppData;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.blockchain.store.playmarket.utilities.SharedPrefsUtil;

import java.io.File;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class InstallPackageReceiver extends BroadcastReceiver {
    private static final String TAG = "InstallPackageReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            String packageName = intent.getData().toString().replace("package:", "");
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_PACKAGE_ADDED)) {
                InstalledAppData appData = SharedPrefsUtil.getDownloadAppIdByPackageName(packageName);
                Log.d(TAG, "onReceive: appId: " + appData);
                if (appData != null)
                    JobUtils.scheduleAppInstallJobs(context, appData.getAppId(), appData.getNode());

            }

            File file = new MyPackageManager().findFileByPackageName(packageName, context);
            if (file != null && file.exists()) {
                file.delete();
            }
            Log.d(TAG, "onReceive: packageName " + packageName);
        } catch (Exception e) {
            Log.d(TAG, "onReceive: Error " + e.getMessage());
        }


    }
}
