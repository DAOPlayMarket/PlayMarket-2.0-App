package com.blockchain.store.playmarket.repositories;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.utilities.AccountManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BalanceRepository {

    public Observable<String> getAccountBalance() {
        return RestApi.getServerApi().getBalance(AccountManager.getAddress().getHex())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
