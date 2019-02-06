package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.InstalledAppData;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;

public class SharedPrefsUtil {
    private static final String SHARED_PREFS_NAME = "prefs_name";

    public static SharedPreferences.Editor getSharedEditor() {
        return getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
    }

    public static SharedPreferences getSharedPrefs() {
        return getContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static Context getContext() {
        return Application.getInstance().getApplicationContext();
    }


    public static InstalledAppData getDownloadAppIdByPackageName(String packageName) {
        HashMap<String, InstalledAppData> map = Hawk.get(Constants.USER_DOWNLOAD_APPS_MAP, new HashMap<>());
        InstalledAppData appId = map.get(packageName);
        try {
            if (appId != null) {
                map.remove(packageName);
                Hawk.put(Constants.USER_DOWNLOAD_APPS_MAP, map);
            }
        } catch (Exception e) {

        }
        return appId;

    }

    public static void addDownloadedApp(String packageName, InstalledAppData installedAppData) {
        HashMap<String, InstalledAppData> map = Hawk.get(Constants.USER_DOWNLOAD_APPS_MAP, new HashMap<>());
        map.put(packageName, installedAppData);
        Hawk.put(Constants.USER_DOWNLOAD_APPS_MAP, map);
    }

}
