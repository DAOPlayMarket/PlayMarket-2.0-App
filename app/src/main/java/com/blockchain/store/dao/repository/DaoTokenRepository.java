package com.blockchain.store.dao.repository;

import com.blockchain.store.dao.data.TokenBalance;
import com.blockchain.store.dao.interfaces.Callbacks;

public class DaoTokenRepository {

    private TokenBalance tokenBalance;
    private Callbacks.DaoTokenCallback callback;
    private long lastRequestTimeMillis = 0;

    public void getDaoTokenBalance(Callbacks.DaoTokenCallback callback) {
        this.callback = callback;
        if ((tokenBalance != null && System.currentTimeMillis() - lastRequestTimeMillis < 600000) || lastRequestTimeMillis != 0) {
            callback.onBalanceReady(tokenBalance);
        } else {
            DaoTransactionRepository.getUserData().subscribe(this::getUserDataSuccess, this::getUserDataFailed);
        }
    }

    public void resetLastRequestTime(){
        lastRequestTimeMillis = 0;
    }

    private void getUserDataSuccess(TokenBalance tokenBalance) {
        lastRequestTimeMillis = System.currentTimeMillis();
        this.tokenBalance = tokenBalance;
        callback.onBalanceReady(tokenBalance);
    }

    private void getUserDataFailed(Throwable throwable) {
        if (tokenBalance != null) {
            callback.onBalanceReady(tokenBalance);
        } else {
            throwable.printStackTrace();
        }
    }

}
