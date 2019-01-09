package com.blockchain.store.dao.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blockchain.store.dao.ui.DaoConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class DaoContractService extends Service {
    private static final String TAG = "DaoContractService";

    /* Methods hex values*/
    private static final String CHANGE_OF_RULES = "e7a31acb9efc5512cc1896266cbc06335ca5e661547b177002b7bb5d8535871e";
    private static final String PAYMENT = "d4f43975feb89f48dd30cabbb32011045be187d1e11c8ea9faa43efc35282519";
    private static final String PROPOSAL_TALLIED = "aaeb6d33bfb00b3e17bf4254a99bbc05ae6215c458924aed1119209ed8fe2416";
    private static final String VOTED = "c34f869b7ff431b034b7b9aea9822dac189a685e0b015c7d1be3add3f89128e8";
    private static final String PROPOSAL_ADDED = "4c6647c4a4058c1a0bcd513fe2522706466700a104b82d4487d888f17ce5be13";

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {
        getContractHistory();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getContractHistory() {
        WebSocket webSocket = createWebSocketConnection(new WebSocketListener());
        webSocket.send("{\"id\":1,\"jsonrpc\":\"2.0\",\"params\":[{\"fromBlock\":\"" + DaoConstants.DAO_BLOCK_NUMBER_HEX + "\",\"toBlock\":\"latest\",\"address\":\"" + DaoConstants.DAO + "\"}],\"method\":\"eth_getLogs\"}");

    }


    private WebSocket createWebSocketConnection(WebSocketListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(DaoConstants.INFURA_WEBSOCKET_MAINNET).build();
        WebSocket webSocket = okHttpClient.newWebSocket(request, listener);
        return webSocket;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class WebSocketListener extends okhttp3.WebSocketListener {

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d(TAG, "onMessage() called with: webSocket = [" + webSocket + "], text = [" + text + "]");
            try {
                JSONArray result = new JSONObject(text).getJSONArray("result");
                for (int i = result.length(); i > 0; i--) {
                    String topics = result.getJSONObject(i).getJSONArray("topics").get(0).toString();
                    switch (topics) {
                        case CHANGE_OF_RULES:
                            break;
                        case PAYMENT:
                            break;
                        case PROPOSAL_TALLIED:
                            break;
                        case VOTED:
                            break;
                        case PROPOSAL_ADDED:
                            break;


                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onMessage(webSocket, text);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.d(TAG, "onClosed() called with: webSocket = [" + webSocket + "], code = [" + code + "], reason = [" + reason + "]");
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.d(TAG, "onFailure() called with: webSocket = [" + webSocket + "], t = [" + t + "], response = [" + response + "]");
            super.onFailure(webSocket, t, response);
        }
    }
}
