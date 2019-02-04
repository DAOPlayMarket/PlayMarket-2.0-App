package com.blockchain.store.playmarket.data.entities;

import android.content.Context;

import com.blockchain.store.playmarket.Application;
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
        Context context = Application.getInstance().getApplicationContext();
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

            case SEND_INTO_REPOSITORY:
                return "Sending tokens into repository";
            case WITHDRAW_TOKEN:
                return "Withdrawing tokens";
            case GET_DIVIDENDS:
                return "Collecting dividends";

            case CREATE_PROPOSAL:
                return "Create proposal transaction pending";
            case VOTE_FOR_PROPOSAL:
                return "Vote for proposal transaction pending";
            case EXECUTE_RPOPOSAL:
                return "Execute proposal transaction prending";
        }
        return "Transaction";
    }

    @Override
    public String getSuccessResultName() {
        Context context = Application.getInstance().getApplicationContext();
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

            case SEND_INTO_REPOSITORY:
                return "Tokens are sent to the repository";
            case WITHDRAW_TOKEN:
                return "Tokens successfully withdrawn";
            case GET_DIVIDENDS:
                return "Dividends successfully collected";

            case CREATE_PROPOSAL:
                return "Proposal created!";
            case VOTE_FOR_PROPOSAL:
                return "You are voted for proposal!";
            case EXECUTE_RPOPOSAL:
                return "Proposal executed";
        }

        return "Unknown transaction complete";
    }

    @Override
    public String getFailedResultName() {
        Context context = Application.getInstance().getApplicationContext();
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
            case SEND_INTO_REPOSITORY:
                return "Sending tokens into repository ends with error";
            case WITHDRAW_TOKEN:
                return "Withdrawing tokens ends with error";
            case GET_DIVIDENDS:
                return "Collecting dividends ends with error";

            case CREATE_PROPOSAL:
                return "Proposal creation failed";
            case VOTE_FOR_PROPOSAL:
                return "Voting for proposal failed";
            case EXECUTE_RPOPOSAL:
                return "Proposal execution failed";
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
        /*DAO*/
        if (transactionTypeOrdinal == Constants.TransactionTypes.SEND_INTO_REPOSITORY.ordinal()) {
            return Constants.TransactionTypes.SEND_INTO_REPOSITORY;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.WITHDRAW_TOKEN.ordinal()) {
            return Constants.TransactionTypes.WITHDRAW_TOKEN;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.GET_DIVIDENDS.ordinal()) {
            return Constants.TransactionTypes.GET_DIVIDENDS;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.CREATE_PROPOSAL.ordinal()) {
            return Constants.TransactionTypes.GET_DIVIDENDS;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.VOTE_FOR_PROPOSAL.ordinal()) {
            return Constants.TransactionTypes.GET_DIVIDENDS;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.EXECUTE_RPOPOSAL.ordinal()) {
            return Constants.TransactionTypes.GET_DIVIDENDS;
        }

        return Constants.TransactionTypes.BUY_APP;
    }
}
