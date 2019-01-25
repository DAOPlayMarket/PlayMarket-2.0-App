package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

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


}
