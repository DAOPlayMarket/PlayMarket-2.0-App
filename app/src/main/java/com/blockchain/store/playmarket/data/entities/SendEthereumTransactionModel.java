package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class SendEthereumTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.TRANSFER.ordinal();
    public String priceInWei;
    public String addressFrom;
    public String addressTo;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.TRANSFER;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public String getTransactionFormattedResult() {
        return "- " + new EthereumPrice(priceInWei).getDisplayPrice(false);
    }

    @Override
    public String getFormattedTitle() {
        return "ETH transfer";
    }
}
