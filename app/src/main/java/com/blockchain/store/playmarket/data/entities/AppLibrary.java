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
    public int versionName;
    public Constants.APP_STATE appState = Constants.APP_STATE.STATE_UNKNOWN;
    public String downloadProgress = "";
    public boolean isSelected = false;

    public String getVersionsAsString(){
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(versionName));
        if (app != null && isHasUpdate) {
            stringBuilder.append(" | ");
            stringBuilder.append(app.version);

        }
        return stringBuilder.toString();
    }


}
