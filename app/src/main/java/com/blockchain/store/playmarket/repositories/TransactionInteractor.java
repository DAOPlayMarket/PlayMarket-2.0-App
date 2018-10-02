package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;

public class TransactionInteractor {

    public static PurchaseAppResponse mapWithJobSchedule(PurchaseAppResponse response) {
        addToJobSchedule(response.hash);
        return response;

    }

    public static PurchaseAppResponse mapWithJobService(PurchaseAppResponse response, TransactionModel transactionModel) {
        transactionModel.transactionHash = response.hash;
        transactionModel.transactionLink = response.link;
        addToJobSchedule(response.hash, transactionModel);
        TransactionPrefsUtil.addToSharePrefs(transactionModel);
        return response;
    }


    private static void addToJobSchedule(String hash) {
        JobUtils.scheduleJob(Application.getInstance().getApplicationContext(), hash, null);
    }

    private static void addToJobSchedule(String hash, TransactionModel transactionModel) {
        JobUtils.scheduleJob(Application.getInstance().getApplicationContext(), hash,transactionModel.getTransactionType());
    }

}
