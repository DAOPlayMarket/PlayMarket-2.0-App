package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class SendReviewTransactionModel extends TransactionModel {
    public AppInfo app;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.SEND_REVIEW;
    }
}
