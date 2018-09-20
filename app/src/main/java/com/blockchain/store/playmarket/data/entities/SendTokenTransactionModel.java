package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class SendTokenTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.TRANSFER_TOKEN.ordinal();
    public AppInfo appInfo;
    public String wasTokenBeforeTransaction;
    public String tokenCurrency;
    public String addressFrom;
    public String tokenCount;
    public String addressTo;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.TRANSFER_TOKEN;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public String getTransactionFormattedResult() {
        return "- " + tokenCount + " " + tokenCurrency;
    }

    @Override
    public String getFormattedTitle() {
        try {
            return "'" + appInfo.getTitleName() + "' Invest";
        } catch (Exception e) {
            return "";
        }

    }
}
