package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class SendEthereumTransactionModel extends TransactionModel {
    public String priceInWei;
    public String addressFrom;
    public String addressTo;

    @Override
    public Constants.TransactionTypes getTransactionStatus() {
        return Constants.TransactionTypes.TRANSFER;
    }
}
