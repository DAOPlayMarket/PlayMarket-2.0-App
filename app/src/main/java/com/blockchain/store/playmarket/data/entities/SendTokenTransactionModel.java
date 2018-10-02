package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class SendTokenTransactionModel extends TransactionModel {
    public AppInfo appInfo;
    public String wasTokenBeforeTransaction;
    public String tokenCurrency;
    public String tokenCount;

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
            return "'" + tokenCurrency + "' Send";
        } catch (Exception e) {
            return "";
        }

    }


}
