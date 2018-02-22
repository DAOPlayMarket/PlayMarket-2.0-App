package com.blockchain.store.playmarket.data.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class ChangellyBaseBody {
    private String jsonrpc = "2.0";
    private int id = 0;
    private String method;
    private HashMap<String, String> params;

    public ChangellyBaseBody(String method, HashMap<String, String> jsonObject) {
        this.method = method;
        params = jsonObject;
    }

    public static ChangellyBaseBody getCurrencies() {
        return new ChangellyBaseBody("getCurrencies", new HashMap<>());
    }

    public static ChangellyBaseBody getMinAmount(String from) {
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("from", from);
            paramObject.put("to", "eth");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("from", from);
        map.put("to", "eth");
        return new ChangellyBaseBody("getMinAmount", map);
    }

//    public static ChangellyBaseBody getTransactionBody(String adress) {
//        return new ChangellyBaseBody("eth_getTransactionCount");
//    }

}
