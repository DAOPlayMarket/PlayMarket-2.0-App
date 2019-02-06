package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.blockchain.store.playmarket.Application;
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

    public static String getEncryptedPassword() {
        if (Hawk.contains(Constants.USER_DOWNLOAD_APPS_MAP)) {
            HashMap<String, String> map = Hawk.get(Constants.USER_DOWNLOAD_APPS_MAP);
            String key = AccountManager.getAddress().getHex();
            return map.get(key);
        } else {
            return null;
        }
    }

    public static String getDownloadAppIdByPackageName(String packageName) {
        HashMap<String, String> map = Hawk.get(Constants.USER_DOWNLOAD_APPS_MAP, new HashMap<>());
        return map.get(packageName);

    }

    public static void addDownloadedApp(String packageName, String appId) {
        HashMap<String, String> map = Hawk.get(Constants.USER_DOWNLOAD_APPS_MAP, new HashMap<>());
        map.put(packageName, appId);
        Hawk.put(Constants.USER_DOWNLOAD_APPS_MAP, map);
    }

}
