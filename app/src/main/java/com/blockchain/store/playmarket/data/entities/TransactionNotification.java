package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.utilities.Constants;

public class TransactionNotification implements NotificationImpl {
    private int id;
    private int transactionTypeOrdinal;

    public TransactionNotification(int id, int transactionType) {
        this.id = id;
        this.transactionTypeOrdinal = transactionType;
    }

    public TransactionNotification(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTitleName() {
        switch (getTransactionTypes()) {
            case BUY_APP:
                return "Buy app transaction pending";
            case INVEST:
                return "Invest transaction pending";
            case TRANSFER:
                return "Transfer transaction pending";
            case TRANSFER_TOKEN:
                return "Token transfer transaction pending";
            case SEND_REVIEW:
                return "Create review transaction pending";
        }
        return "Transaction";
    }

    @Override
    public String getSuccessResultName() {
        switch (getTransactionTypes()) {
            case BUY_APP:
                return "Buy app transaction complete with success";
            case INVEST:
                return "Invest transaction complete with success";
            case TRANSFER:
                return "Transfer transaction complete with success";
            case TRANSFER_TOKEN:
                return "Token transfer transaction complete with success";
            case SEND_REVIEW:
                return "Create review transaction complete with success";
        }

        return "Unknown transaction complete";
    }

    @Override
    public String getFailedResultName() {
        switch (getTransactionTypes()) {
            case BUY_APP:
                return "Buy app transaction complete with failure";
            case INVEST:
                return "Invest transaction complete with failure";
            case TRANSFER:
                return "Transfer transaction complete with failure";
            case TRANSFER_TOKEN:
                return "Token transfer transaction complete with failure";
            case SEND_REVIEW:
                return "Create review transaction complete with failure";
        }
        return "Unknown transaction error";
    }

    private Constants.TransactionTypes getTransactionTypes() {
        if (transactionTypeOrdinal == Constants.TransactionTypes.BUY_APP.ordinal()) {
            return Constants.TransactionTypes.BUY_APP;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.INVEST.ordinal()) {
            return Constants.TransactionTypes.INVEST;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.TRANSFER.ordinal()) {
            return Constants.TransactionTypes.TRANSFER;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.TRANSFER_TOKEN.ordinal()) {
            return Constants.TransactionTypes.TRANSFER_TOKEN;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.SEND_REVIEW.ordinal()) {
            return Constants.TransactionTypes.SEND_REVIEW;
        }
        return Constants.TransactionTypes.BUY_APP;
    }
}
