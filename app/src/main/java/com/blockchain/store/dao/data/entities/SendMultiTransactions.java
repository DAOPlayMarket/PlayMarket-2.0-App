package com.blockchain.store.dao.data.entities;

import com.blockchain.store.playmarket.data.entities.TransactionModel;
import com.blockchain.store.playmarket.utilities.Constants;

public class SendMultiTransactions extends TransactionModel {
    private String firstSignedTransaction;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return null;
    }

    @Override
    public String getTransactionFormattedResult() {
        return null;
    }

    @Override
    public String getFormattedTitle() {
        return null;
    }

    @Override
    public boolean isPositive() {
        return false;
    }
}
