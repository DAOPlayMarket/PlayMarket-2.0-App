package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class AppBuyTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.BUY_APP.ordinal();
    public String priceInWei;
    public App boughtApp;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.BUY_APP;
    }
}
