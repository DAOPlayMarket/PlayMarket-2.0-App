package com.blockchain.store.playmarket.data.entities;

import java.text.DecimalFormat;

public class UserBalance {
    public String balanceInWei;
    public String balanceInLocalCurrency;
    public String balanceInPMC;
    public String symbol;

    public String getFormattedLocalCurrency(){
        return String.format("%.2f", balanceInLocalCurrency);
    }
}
