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

    public static void putTransaction(TransactionModel transactionModel) {
        ArrayList<TransactionModel> allTransaction = getAllTransaction();
        allTransaction.add(0, transactionModel);
        getSharedEditor().putString(Constants.TRANSACTION_MODEL_KEY, new Gson().toJson(allTransaction)).apply();

    }

    public static ArrayList<TransactionModel> getAllTransaction() {
        SharedPreferences sharedPrefs = getSharedPrefs();
        String string = sharedPrefs.getString(Constants.TRANSACTION_MODEL_KEY, "");
        ArrayList<TransactionModel> transactionModels = new Gson().fromJson(string, new TypeToken<ArrayList<TransactionModel>>() {
        }.getType());
        if (transactionModels == null) {
            return new ArrayList<>();
        }
        return transactionModels;
    }
}
