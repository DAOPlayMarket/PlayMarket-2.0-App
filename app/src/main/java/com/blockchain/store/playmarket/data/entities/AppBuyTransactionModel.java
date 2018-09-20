package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.Constants;

public class AppBuyTransactionModel extends TransactionModel {
    public final int TransactionType = Constants.TransactionTypes.BUY_APP.ordinal();
    public String priceInWei;
    public App boughtApp;

    @Override
    public Constants.TransactionTypes getTransactionType() {
        return Constants.TransactionTypes.BUY_APP;
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
        return "'" + boughtApp.getTitleName() + "' Buy";
    }
}
