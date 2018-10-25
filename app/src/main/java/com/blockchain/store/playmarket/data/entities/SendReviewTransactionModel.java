package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.utilities.Constants;

public class SendReviewTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.SEND_REVIEW.ordinal();
    public AppInfo app;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.SEND_REVIEW;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public String getTransactionFormattedResult() {
        return "";
    }

    @Override
    public String getFormattedTitle() {
        return "'" + app.getTitleName() + "' Comment";
    }


}
