package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class SendTokenTransactionModel extends TransactionModel {
    public String wasTokenBeforeTransaction;
    public String tokenCurrency;
    public String addressFrom;
    public String tokenCount;
    public String addressTo;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.TRANSFER_TOKEN;
    }
}
