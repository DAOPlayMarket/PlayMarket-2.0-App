package com.blockchain.store.playmarket.data.entities;

import android.content.pm.ApplicationInfo;

import com.blockchain.store.playmarket.utilities.MyPackageManager;

public class AppLibrary {
    public App app;
    public ApplicationInfo applicationInfo;

    public boolean isHasUpdate() {
        try {
            String version = app.version;
            int versionNameByPackageName = MyPackageManager.getVersionNameByPackageName(app.packageName);
            return versionNameByPackageName < Integer.parseInt(app.version);
        } catch (Exception e) {

        }

        return false;
    }

}
