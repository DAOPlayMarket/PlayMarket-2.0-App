package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.TransactionPrefsUtil;
import com.orhanobut.hawk.Hawk;

public class TransactionInteractor {

    public static String mapWithJobService(String hash, TransactionModel transactionModel) {

        transactionModel.transactionHash = hash;
        transactionModel.transactionLink = Constants.ETHER_SCAN_TX_URL + hash;
        addToJobSchedule(hash, transactionModel);
        TransactionPrefsUtil.addToSharePrefs(transactionModel);
        return hash;
    }

    public static void addToJobSchedule(String firstHash, String secondHash, String secondRawTransaction, Constants.TransactionTypes transactionType) {
        JobUtils.scheduleCheckTransactionJobs(Application.getInstance(), firstHash, secondHash, secondRawTransaction, transactionType);
    }

    public static void addToJobSchedule(String hash, Constants.TransactionTypes transactionTypes) {
        JobUtils.scheduleCheckTransactionJob(Application.getInstance().getApplicationContext(), hash, transactionTypes);
    }

    private static void addToJobSchedule(String hash, TransactionModel transactionModel) {
        JobUtils.scheduleCheckTransactionJob(Application.getInstance().getApplicationContext(), hash, transactionModel.getTransactionType());
    }

}
