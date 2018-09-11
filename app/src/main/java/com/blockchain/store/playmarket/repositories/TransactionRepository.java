package com.blockchain.store.playmarket.repositories;

import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.check_transation_status_beta.JobUtils;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.utilities.AccountManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionRepository {

    public TransactionRepository sendTrasaction(TransactionRepositoryCallback callback,Observable<PurchaseAppResponse> mapFunction) {
        Observable<PurchaseAppResponse> observable = RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .zipWith(RestApi.getServerApi().getGasPrice(), Pair::new)
                .subscribeOn(Schedulers.newThread())
                .flatMap(result -> mapFunction)
                .observeOn(AndroidSchedulers.mainThread());
        return this;
    }

    public TransactionRepository map


    public void proceedSubscription(Observable<PurchaseAppResponse> subscription, TransactionRepositoryCallback callback) {
        subscription.subscribe(result -> onTransactionReady(result, callback), error -> onTransactionError(error, callback));

    }

    private void onTransactionError(Throwable throwable, TransactionRepositoryCallback callback) {
        callback.onTransactionError(throwable);
    }

    private void onTransactionReady(PurchaseAppResponse purchaseAppResponse, TransactionRepositoryCallback callback) {
        callback.onTransactionReady(purchaseAppResponse);
        JobUtils.schduleJob(Application.getInstance().getApplicationContext(), purchaseAppResponse.hash);
    }


    public interface TransactionRepositoryCallback {

        void onTransactionReady(PurchaseAppResponse purchaseAppResponse);

        void onTransactionError(Throwable throwable);
    }
}
