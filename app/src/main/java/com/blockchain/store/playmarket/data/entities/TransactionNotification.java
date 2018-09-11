package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.interfaces.NotificationImpl;

public class TransactionNotification implements NotificationImpl {
    private int id;

    public TransactionNotification(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitleName() {
        return "Transaction";
    }
}
