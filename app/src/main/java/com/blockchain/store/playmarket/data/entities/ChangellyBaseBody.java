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
    public static ChangellyBaseBody getCurrenciesFull() {
        return new ChangellyBaseBody("getCurrenciesFull", new HashMap<>());
    }

    public static ChangellyBaseBody getMinAmount(String from) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from", from);
        map.put("to", "eth");
        return new ChangellyBaseBody("getMinAmount", map);
    }

    public static ChangellyBaseBody getExchangeAmount(String from, String amount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("from", from);
        map.put("to", "eth");
        map.put("amount", amount);
        return new ChangellyBaseBody("getMinAmount", map);
    }

}
