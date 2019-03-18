package com.blockchain.store.playmarket.data.entities;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.blockchain.store.playmarket.utilities.Constants;

public class AppLibrary {
    public App app;
    public ApplicationInfo applicationInfo;
    public Drawable icon;
    public String title;
    public boolean isHasUpdate = false;
    public int versionCode;
    public String versionName;
    public Constants.APP_STATE appState = Constants.APP_STATE.STATE_UNKNOWN;
    public String downloadProgress = "";
    public boolean isSelected = false;

    public String getVersionsAsString(){
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(versionName));
        if (app != null && isHasUpdate) {
            stringBuilder.append(" | ");
            stringBuilder.append(app.versionName);
        }
        return stringBuilder.toString();
    }

    public String getNewAppVersion() {
        if (app != null && (isHasUpdate || appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED)) {
            if (app.versionName != null) {
                return app.versionName;
            } else {
                return app.version;
            }
        } else {
            return "";
        }
    }

    public String getSizeAsMBString(){
        if (app != null && (isHasUpdate || appState == Constants.APP_STATE.STATE_UPDATE_DOWNLOADED_NOT_INSTALLED) && app.size != null) {
            double sizeInByte = Double.parseDouble(app.size);
            double sizeInMB = Math.round(sizeInByte / 1048576 * 100d) / 100d;
            return String.valueOf(sizeInMB) + " MB";
        } else {
            return "";
        }

    }

}
