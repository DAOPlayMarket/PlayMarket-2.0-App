package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants.TransactionStatus;
import com.blockchain.store.playmarket.utilities.Constants.TransactionTypes;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public abstract class TransactionModel {
    public TransactionStatus transactionStatus;
    public TransactionTypes transactionTypes;
    public TransactionReceipt transactionReceipt;

    public String transactionHash;
    public String transactionFee;
    public String transactionLink;

    public Long timeStamp;

}
