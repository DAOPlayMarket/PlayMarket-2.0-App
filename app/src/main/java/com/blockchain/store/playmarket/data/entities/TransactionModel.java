package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.Constants.TransactionStatus;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public abstract class TransactionModel {
    public abstract Constants.TransactionTypes getTransactionType();

    public abstract String getTransactionFormattedResult();

    public abstract String getFormattedTitle();

    public abstract boolean isPositive();

    public TransactionStatus transactionStatus = TransactionStatus.PENDING;
    public Long timeStamp = System.currentTimeMillis();

    public TransactionReceipt transactionReceipt;
    public String transactionHash;
    public String transactionFee;
    public String transactionLink;


    public String getDetailedInfo() {
        return "";
    }

}
