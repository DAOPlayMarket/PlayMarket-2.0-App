package com.blockchain.store.playmarket.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.utilities.MyPackageManager;

import java.io.File;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class InstallPackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getEncodedSchemeSpecificPart();

        File file = new MyPackageManager().findFileByPackageName(packageName, context);
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}
