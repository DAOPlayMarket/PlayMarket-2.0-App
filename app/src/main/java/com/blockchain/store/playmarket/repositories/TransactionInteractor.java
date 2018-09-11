package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;

public class TransactionInteractor {

    public static PurchaseAppResponse mapWithJobSchedule(PurchaseAppResponse response) {
        JobUtils.schduleJob(Application.getInstance().getApplicationContext(), response.hash);
        return response;

    }

    private void onTransactionError(Throwable throwable, TransactionRepositoryCallback callback) {
        callback.onTransactionError(throwable);
    }

    private void onTransactionReady(PurchaseAppResponse purchaseAppResponse, TransactionRepositoryCallback callback) {
        callback.onTransactionReady(purchaseAppResponse);
    }


    public interface TransactionRepositoryCallback {

        void onTransactionReady(PurchaseAppResponse purchaseAppResponse);

        void onTransactionError(Throwable throwable);
    }
}
