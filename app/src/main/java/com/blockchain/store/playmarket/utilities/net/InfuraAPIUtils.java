package com.blockchain.store.playmarket.utilities.net;

import android.util.Log;

import com.blockchain.store.playmarket.data.types.EthereumPrice;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.datatypes.Int;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by samsheff on 07/11/2017.
 */

public class InfuraAPIUtils {

    public static InfuraAPIUtils api;

    public String APIURL = "https://api.infura.io/v1/jsonrpc/";
    public String Network = "ropsten";
    public String GetBalanceMethod = "eth_getBalance";
    public String ACCESS_TOKEN = "8sue7GAfxIFcwebGXyye";

    public EthereumPrice balance;

    public InfuraAPIUtils() {
        this.APIURL += Network + "/";

        api = this;
    }

    public String getBalance(String address) throws IOException {
        HttpClient client = createHttpClient();
        HttpGet request = new HttpGet(APIURL + GetBalanceMethod + "?params=%5B%22" + address + "%22%2C%22latest%22%5D&token=" + ACCESS_TOKEN);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        String balance = "";
        try {
            balance = String.valueOf(Integer.parseInt(new JSONObject(responseBody).getString("result"), 8));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NET", "Get Balance: " + balance);

        return balance;
    }

    public static HttpClient createHttpClient() {
        return new DefaultHttpClient();
    }
}
