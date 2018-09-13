package com.blockchain.store.playmarket.repositories;

import android.content.Context;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.orhanobut.hawk.Hawk;

public class TransactionInteractor {

    public static PurchaseAppResponse mapWithJobSchedule(PurchaseAppResponse response) {
        addToJobSchedule(Application.getInstance().getApplicationContext(), response.hash);
        return response;

    }

    public static PurchaseAppResponse mapWithJobService(PurchaseAppResponse response, AppInfo appInfo) {
        addToJobSchedule(Application.getInstance().getApplicationContext(), response.hash);
        return response;
    }

    private static void addToJobSchedule(Context applicationContext, String hash) {
        JobUtils.scheduleJob(Application.getInstance().getApplicationContext(), hash);
    }

}
