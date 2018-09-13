package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class InvestTransactionModel extends TransactionModel {
    public AppInfo appInfo;
    public String price;

    @Override
    public Constants.TransactionTypes getTransactionStatus() {
        return Constants.TransactionTypes.INVEST;
    }
}
