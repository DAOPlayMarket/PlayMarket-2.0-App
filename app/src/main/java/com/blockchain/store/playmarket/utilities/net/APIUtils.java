package com.blockchain.store.playmarket.utilities.net;

import android.os.Environment;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.ethmobile.ethdroid.KeyManager;

/**
 * Created by samsheff on 05/09/2017.
 */

public class APIUtils {

    public static APIUtils api;

    public static final String PLAYMARKET_BASE_URL = ".playmarket.io";
    public static final String GET_ADDRESS_CONTRACT_URL = "/v2/getAddressContract";
    public static final String GET_BALANCE_URL = "/v2/getBalance";
    public static final String GET_NONCE_URL = "/v2/getTransactionCount";
    public static final String GET_GAS_PRICE_URL = "/v2/getGasPrice";
    public static final String SEND_TX_URL = "/v2/sendRawTransaction";
    public static final String SEND_TX_ICO_URL = "/v2/sendRawTransactionICO";
    public static final String GET_APP_URL = "/v2/getApp";
    public static final String GET_APK_URL = "/v2/loadApp";
    public static final String LOAD_PICTURE_URL = "/v2/loadPicture";

    public static final String CATEGORY_ID_PARAM = "idCTG";
    public static final String GET_APP_PARAM = "getApp";
    public static final String START_ID_PARAM = "startIdApp";
    public static final String COUNT_PARAM = "Count";
    public static final String ADDRESS_PARAM = "address";
    public static final String BALANCE_PARAM = "balance";
    public static final String GET_TRANSACTION_COUNT_PARAM = "getTransactionCount";
    public static final String SERIALIZED_TRANSACTION_PARAM = "serializedTx";
    public static final String GAS_PRICE_PARAM = "gasPrice";

    public static String nodeUrl = "https://n";

    private static final int BUFFER_SIZE = 4096;

    public EthereumPrice balance;

    public APIUtils(String node) {
        this.nodeUrl = nodeUrl + node + PLAYMARKET_BASE_URL;

        api = this;
    }

    public String getContractAddress() throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_ADDRESS_CONTRACT_URL);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        String address = "";
        try {
            address = new JSONObject(responseBody).getString(ADDRESS_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NET", "Get Contract Address: " + address);

        return address;
    }

    public String getBalance(String address) throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_BALANCE_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair(ADDRESS_PARAM, address));

        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        String balance = "";
        try {
            balance = new JSONObject(responseBody).getString(BALANCE_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NET", "Get Balance: " + balance);

        return balance;
    }

    public String getGasPrice() throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_GAS_PRICE_URL);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", "Get Gas Price: " + responseBody);

        String gasPrice = "0";
        try {
            gasPrice = new JSONObject(responseBody).getString(GAS_PRICE_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gasPrice;
    }

    public int getNonce(String address) throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_NONCE_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair(ADDRESS_PARAM, address));

        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        int nonce = 0;
        try {
            nonce = new JSONObject(responseBody).getInt(GET_TRANSACTION_COUNT_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NET", "Get Nonce: " + nonce);

        return nonce;
    }

    public boolean sendTX(String rawTransaction) throws IOException {
        HttpClient client = createHttpClient();
        HttpGet request = new HttpGet(getSendTxIcoLink("0x" + rawTransaction));

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        String status = "";
        try {
            status = new JSONObject(responseBody).getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NET", "Send TX: " + status);

        return true;
    }

    public File sendTXAndDownload(String rawTransaction) throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + SEND_TX_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair(SERIALIZED_TRANSACTION_PARAM, "0x" + rawTransaction));

        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        response = client.execute(request);

        String saveFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File file = new File(saveFilePath);
        file.mkdirs();
        File outputFile = new File(file, "app.apk");
        if (outputFile.exists()) {
            outputFile.delete();
        }

        InputStream inputStream = response.getEntity().getContent();

        // opens an output stream to save into file
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();


        return outputFile;
    }

    public JSONArray start() throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_APP_URL);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", "Get App: " + responseBody);

        JSONArray apps = new JSONArray();
        try {
            apps = new JSONObject(responseBody).getJSONArray(GET_APP_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return apps;
    }

    public JSONArray getCategory(String category) throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_APP_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair(CATEGORY_ID_PARAM, category));

        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", "Get App: " + responseBody);

        JSONArray apps = new JSONArray();
        try {
            apps = new JSONObject(responseBody).getJSONArray(GET_APP_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return apps;
    }

    public JSONArray pageCategory(String category, int startId, int count) throws IOException {
        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(nodeUrl + GET_APP_URL);

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair(CATEGORY_ID_PARAM, category));
        nameValuePair.add(new BasicNameValuePair(START_ID_PARAM, String.valueOf(startId)));
        nameValuePair.add(new BasicNameValuePair(COUNT_PARAM, String.valueOf(count)));

        //Encoding POST data
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", "Get App: " + responseBody);

        JSONArray apps = new JSONArray();
        try {
            apps = new JSONObject(responseBody).getJSONArray(GET_APP_PARAM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return apps;
    }

    public void updateBalance(final KeyManager keyManager) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String balanceString = String.valueOf(APIUtils.api.getBalance(keyManager.getAccounts().get(0).getAddress().getHex()));

                    balance = new EthereumPrice(balanceString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static String getApkLink(String address, String idApp, String idCat, String hashIPFS) {
        Log.d("NET", nodeUrl + GET_APK_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS);
        return nodeUrl + GET_APK_URL + "?address=" + address + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS;
    }

    public static String getSendTxLink(String tx, String idApp, String idCat, String hashIPFS) {
        Log.d("NET", nodeUrl + SEND_TX_URL + "?serializedTx=" + tx + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS);
        return nodeUrl + SEND_TX_URL + "?serializedTx=" + tx + "&idApp=" + idApp + "&idCTG=" + idCat + "&hashIpfs=" + hashIPFS;
    }

    public static String getSendTxIcoLink(String tx) {
        Log.d("NET", nodeUrl + SEND_TX_ICO_URL + "?serializedTx=" + tx);
        return nodeUrl + SEND_TX_ICO_URL + "?serializedTx=" + tx;
    }

    public String generateMarkdownForImages(String hash, int count) {
        String markdown = "\n\n\n<span style=\"display:block;text-align:center;width:100%;\">";
        for (int i = 1; i <= count; i++) {
            markdown += "![Test](" + nodeUrl + LOAD_PICTURE_URL + "?hashIpfs=" + hash + "&picture=" + i + ".jpg)\n";
        }

        markdown += "</span>";

        Log.d("NET", markdown);

        return markdown;
    }

    public String generateMarkdownForThumbnail(String hash) {
        String markdown = "\n\n\n";
        markdown += "<img src='" + nodeUrl + LOAD_PICTURE_URL + "?hashIpfs=" + hash + "&picture=thumbnail.jpg' width='100%'>\n";
        markdown += "\n";

        return markdown;
    }

    public static HttpClient createHttpClient() {
        return new DefaultHttpClient();
    }

}
