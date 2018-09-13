package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants.TransactionStatus;
import com.blockchain.store.playmarket.utilities.Constants.TransactionTypes;

public abstract class TransactionModel {
    public String transactionHash;
    public TransactionTypes transactionTypes;
    public TransactionStatus transactionStatus;

}
