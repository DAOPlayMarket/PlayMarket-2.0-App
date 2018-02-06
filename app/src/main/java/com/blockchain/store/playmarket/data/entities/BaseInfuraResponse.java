package com.blockchain.store.playmarket.data.entities;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class BaseInfuraResponse {
    public String jsonrpx;
    public int id;
    public String result;

    public long getResult() {
        if (result.startsWith("0x")) {
            return Long.parseLong(result.replace("0x", ""), 16);
        } else {
            return Long.parseLong(result, 16);
        }

    }
}
