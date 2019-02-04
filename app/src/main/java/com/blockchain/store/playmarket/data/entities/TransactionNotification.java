package com.blockchain.store.playmarket.data.entities;

import android.content.Context;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
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
                return context.getString(R.string.buy_app_tx_pending);
            case INVEST:
                return context.getString(R.string.invest_tx_pending);
            case TRANSFER:
                return context.getString(R.string.transfer_tx_pending);
            case TRANSFER_TOKEN:
                return context.getString(R.string.token_transfer_tx_pending);
            case SEND_REVIEW:
                return context.getString(R.string.create_review_tx_pending);
            case SEND_INTO_REPOSITORY:
                return context.getString(R.string.sending_tokens_into_repository);
            case WITHDRAW_TOKEN:
                return context.getString(R.string.withdraw_tokens);
            case GET_DIVIDENDS:
                return context.getString(R.string.collecting_dividends);
            case CREATE_PROPOSAL:
                return context.getString(R.string.create_proposal_tx_pending);
            case VOTE_FOR_PROPOSAL:
                return context.getString(R.string.vote_for_proposal_tx_pending);
            case EXECUTE_RPOPOSAL:
                return context.getString(R.string.execute_proposal_tx_pending);
        }
        return "";
    }

    @Override
    public String getSuccessResultName() {
        Context context = Application.getInstance().getApplicationContext();
        switch (getTransactionTypes()) {
            case BUY_APP:
                return context.getString(R.string.buy_app_tx_success);
            case INVEST:
                return context.getString(R.string.invest_tx_success);
            case TRANSFER:
                return context.getString(R.string.transfer_tx_success);
            case TRANSFER_TOKEN:
                return context.getString(R.string.token_transfer_tx_success);
            case SEND_REVIEW:
                return context.getString(R.string.create_review_tx_success);

            case SEND_INTO_REPOSITORY:
                return context.getString(R.string.token_are_sent);
            case WITHDRAW_TOKEN:
                return context.getString(R.string.tokenst_withdraw_tx_success);
            case GET_DIVIDENDS:
                return context.getString(R.string.dividends_collect__tx_success);

            case CREATE_PROPOSAL:
                return context.getString(R.string.proposal_created_tx_success);
            case VOTE_FOR_PROPOSAL:
                return context.getString(R.string.you_are_voted__tx_success);
            case EXECUTE_RPOPOSAL:
                return context.getString(R.string.proposal_executed_tx_success);
        }

        return "";
    }

    @Override
    public String getFailedResultName() {
        Context context = Application.getInstance().getApplicationContext();
        switch (getTransactionTypes()) {
            case BUY_APP:
                return context.getString(R.string.buy_app_tx_failed);
            case INVEST:
                return context.getString(R.string.invest_tx_failed);
            case TRANSFER:
                return context.getString(R.string.transfer_tx_failed);
            case TRANSFER_TOKEN:
                return context.getString(R.string.token_transfer_tx_failed);
            case SEND_REVIEW:
                return context.getString(R.string.create_review_tx_failed);
            case SEND_INTO_REPOSITORY:
                return context.getString(R.string.sending_token_tx_failed);
            case WITHDRAW_TOKEN:
                return context.getString(R.string.withdrawing_token_tx_failed);
            case GET_DIVIDENDS:
                return context.getString(R.string.collecting_dividends_tx_failed);

            case CREATE_PROPOSAL:
                return context.getString(R.string.proposal_creation_tx_failed);
            case VOTE_FOR_PROPOSAL:
                return context.getString(R.string.votint_for_proposal_failed_tx_failed);
            case EXECUTE_RPOPOSAL:
                return context.getString(R.string.proposal_execution_tx_failed);
        }
        return "";
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
            return Constants.TransactionTypes.CREATE_PROPOSAL;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.VOTE_FOR_PROPOSAL.ordinal()) {
            return Constants.TransactionTypes.VOTE_FOR_PROPOSAL;
        }
        if (transactionTypeOrdinal == Constants.TransactionTypes.EXECUTE_RPOPOSAL.ordinal()) {
            return Constants.TransactionTypes.EXECUTE_RPOPOSAL;
        }

        return Constants.TransactionTypes.BUY_APP;
    }
}
