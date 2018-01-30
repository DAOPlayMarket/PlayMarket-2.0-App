package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class InfuraUserBalanceBody {
    private String jsonrxp = "1.0";
    private int id = 1;
    private String method = "eth_getBalance";
    private ArrayList<String> params = new ArrayList<>();

    public InfuraUserBalanceBody(String address) {
        params.add(address);
        params.add("latest");
    }
}
