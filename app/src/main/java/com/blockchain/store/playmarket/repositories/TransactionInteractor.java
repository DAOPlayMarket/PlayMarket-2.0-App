package com.blockchain.store.playmarket.repositories;

import android.content.Context;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.SharedPrefsUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class TransactionInteractor {

    public static PurchaseAppResponse mapWithJobSchedule(PurchaseAppResponse response) {
        addToJobSchedule(Application.getInstance().getApplicationContext(), response.hash);
        return response;

    }

    public static PurchaseAppResponse mapWithJobService(PurchaseAppResponse response, TransactionModel transactionModel) {
        addToJobSchedule(Application.getInstance().getApplicationContext(), response.hash);
        transactionModel.transactionHash = response.hash;
        transactionModel.transactionLink = response.link;
        SharedPrefsUtils.addToSharePrefs(transactionModel);
        return response;
    }


    private static void addToJobSchedule(Context applicationContext, String hash) {
        JobUtils.scheduleJob(Application.getInstance().getApplicationContext(), hash);
    }

}
