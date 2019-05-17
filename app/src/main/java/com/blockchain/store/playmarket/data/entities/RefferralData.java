package com.blockchain.store.playmarket.data.entities;

public class RefferralData {
    public RefferralData(String packageName, String payload) {
        this.packageName = packageName;
        this.payload = payload;
    }

    String packageName;
    String payload;
}
