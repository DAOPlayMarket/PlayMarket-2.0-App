package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class InvestTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.INVEST.ordinal();
    public AppInfo appInfo;
    public String price;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.INVEST;
    }
}
