package com.blockchain.store.playmarket.data.entities;

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
    public String currencyStock; //todo
}
