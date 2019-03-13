package com.blockchain.store.playmarket.ui.pex_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.dapps.Web3View;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.google.gson.JsonArray;

import org.ethereum.geth.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

public class DappActivity extends AppCompatActivity {
    private static final String TAG = "DappActivity";

    @BindView(R.id.web_view) Web3View webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp);
        ButterKnife.bind(this);
        setWebView();
    }

    private void setWebView() {
        DWebView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptObject(new JsApi(this), "");
//        webView.addJavascriptInterface();
        webView.loadUrl("http://192.168.88.230:8080");
//        webView.loadUrl("https://playtowin.io/");
//        webView.loadUrl("https://smartz.io/");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class JsApi {
        private Context context;

        public JsApi(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void getAccounts(Object abc, CompletionHandler handler) {
            Log.d(TAG, "getAccounts() called with: handler = [" + handler + "]" + abc);
            handler.complete(AccountManager.getAccount().getAddress().getHex());
        }


        @JavascriptInterface
        public void signTx(Object tx, CompletionHandler handler) {
            Log.d(TAG, "signTx() called with: tx = [" + tx + "], handler = [" + handler + "]");
            Toast.makeText(context, tx + " received", Toast.LENGTH_SHORT).show();
            /*
            1. show dialog;
            2. sign tx
            *
            * */

//            new DialogManager().showConfirmDialog(context, new Callbacks.PasswordCallback() {
//                @Override
//                public void onAccountUnlock(Boolean isUnlock) {
//
//                }
//            });

            handler.complete(tx);
        }


        @JavascriptInterface
        public void sendTransaction(Object tx, CompletionHandler handler) {
            Log.d(TAG, "sendTransaction() called with: tx = [" + tx + "], handler = [" + handler + "]");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(tx.toString());
                Transaction transaction = new Transaction(jsonObject.toString());
                Log.d(TAG, "sendTransaction: ");
            } catch (Exception e) {
                e.printStackTrace();
            }

            handler.complete("0x3e386e93cd9be7eda8e6780de46990b89a69f1157c45e7b4626cbc7e39e9037d");

        }
    }
}
