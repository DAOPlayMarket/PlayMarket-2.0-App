package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class InfuraUserBaseBody {
    private String jsonrxp = "2.0";
    private int id = 2;
    private String method = "eth_getBalance";
    private ArrayList<String> params = new ArrayList<>();

    public InfuraUserBaseBody(String address) {
        params.add(address);
        params.add("latest");
    }
}
