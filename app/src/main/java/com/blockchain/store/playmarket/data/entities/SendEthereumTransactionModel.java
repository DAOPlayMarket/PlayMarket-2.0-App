package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class SendEthereumTransactionModel extends TransactionModel {
    public String weiCount;
    public String addressFrom;
    public String addressTo;
    public Long timeStamp;

    public String wasTokenBeforeTransaction;

    @Override
    public Constants.TransactionTypes getTransactionStatus() {
        return Constants.TransactionTypes.TRANSFER;
    }
}
