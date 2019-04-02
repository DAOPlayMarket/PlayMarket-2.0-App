package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Crypton04 on 08.02.2018.
 */

public class AccountInfoResponse {
    public String balance;
    @SerializedName("countTx")
    public int count;
    public String gasPrice;
    public String adrNode;
    public String currencyStock;


    public String getGasPrice() {
        long currentGasPrice = Long.valueOf(gasPrice);
        long additionalGwei = new EthereumPrice("1", EthereumPrice.Currency.GWEI).wei.longValue();
        return String.valueOf(currentGasPrice + additionalGwei);
    }

    public double getCurrentStock() {
        try {
            return Double.parseDouble(currencyStock);
        } catch (Exception e) {
            return 1f;
        }

    }
}
