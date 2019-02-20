package com.blockchain.store.playmarket.interfaces;

public interface NotificationImpl {

    int getId();

    String getTitleName();

    default String getSuccessResultName() {
        return getTitleName();
    }

    default String getFailedResultName() {
        return getTitleName();
    }



}
