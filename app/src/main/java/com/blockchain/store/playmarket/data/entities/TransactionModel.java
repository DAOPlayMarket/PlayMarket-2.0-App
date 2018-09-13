package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.Constants.TransactionStatus;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public abstract class TransactionModel {
    public abstract Constants.TransactionTypes getTransactionStatus();

    public TransactionStatus transactionStatus = TransactionStatus.PENDING;
    public TransactionReceipt transactionReceipt;

    public String transactionHash;
    public String transactionFee;
    public String transactionLink;

    public Long timeStamp = System.currentTimeMillis();

}
