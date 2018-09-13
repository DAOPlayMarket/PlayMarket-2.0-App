package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class AppBuyTransactionModel extends TransactionModel {
    public String priceInWei;
    public App boughtApp;

    @Override
    public Constants.TransactionTypes getTransactionStatus() {
        return Constants.TransactionTypes.BUY_APP;
    }
}
