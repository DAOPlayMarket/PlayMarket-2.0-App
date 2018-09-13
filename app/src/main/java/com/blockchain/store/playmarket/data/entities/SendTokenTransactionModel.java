package com.blockchain.store.playmarket.data.entities;

public class SendTokenTransactionModel extends TransactionModel {
    public String tokenCount;
    public String addressFrom;
    public String addressTo;
    public String tokenCurrency;
    public Long timeStamp;

    public String wasTokenBeforeTransaction;

}
