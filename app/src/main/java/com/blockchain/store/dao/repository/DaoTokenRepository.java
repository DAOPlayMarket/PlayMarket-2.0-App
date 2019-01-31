package com.blockchain.store.dao.repository;

import android.util.Pair;

import com.blockchain.store.dao.interfaces.Callbacks;

public class DaoTokenRepository {

    private Pair<String, String> tokenBalancePair;
    private Callbacks.DaoTokenCallback callback;
    private long lastRequestTimeMillis = 0;

    public void getDaoTokenBalance(Callbacks.DaoTokenCallback callback) {
        this.callback = callback;
        if (tokenBalancePair != null && System.currentTimeMillis() - lastRequestTimeMillis < 600000) {
            callback.onBalanceReady(tokenBalancePair);
        } else {
            DaoTransactionRepository.getUserData().subscribe(this::getUserDataSuccess, this::getUserDataFailed);
        }
    }

    private void getUserDataSuccess(Pair<String, String> stringStringPair) {
        lastRequestTimeMillis = System.currentTimeMillis();
        tokenBalancePair = stringStringPair;
        callback.onBalanceReady(tokenBalancePair);
    }

    private void getUserDataFailed(Throwable throwable) {
        if (tokenBalancePair != null) {
            callback.onBalanceReady(tokenBalancePair);
        } else {
            throwable.printStackTrace();
        }
    }

}
