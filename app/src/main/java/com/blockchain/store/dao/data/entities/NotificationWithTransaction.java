package com.blockchain.store.dao.data.entities;

import com.blockchain.store.playmarket.interfaces.NotificationImpl;

public class NotificationWithTransaction implements NotificationImpl {

    private String signedTransaction;


    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getTitleName() {
        return null;
    }

    @Override
    public String getSuccessResultName() {
        return null;
    }

    @Override
    public String getFailedResultName() {
        return null;
    }
}
