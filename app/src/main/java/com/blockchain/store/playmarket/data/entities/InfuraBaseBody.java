package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class InfuraBaseBody {
    private String jsonrxp = "2.0";
    private int id = 10;
    private String method;
    private ArrayList<String> params = new ArrayList<>();

    public InfuraBaseBody(String address, String method) {
        this.method = method;
        params.add(address);
        params.add("latest");
    }

    public static InfuraBaseBody getBalanceBody(String adress) {
        return new InfuraBaseBody(adress, "eth_getBalance");
    }

    public static InfuraBaseBody getGasPriceBody(String adress) {
        return new InfuraBaseBody(adress, "eth_gasPrice");
    }

    public static InfuraBaseBody getTransactionBody(String adress) {
        return new InfuraBaseBody(adress, "eth_getTransactionCount");
    }

}
