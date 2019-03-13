package com.blockchain.store.playmarket.ui.pex_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blockchain.store.dao.interfaces.Callbacks;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

public class DappActivity extends AppCompatActivity {
    private static final String TAG = "DappActivity";

    @BindView(R.id.web_view) DWebView webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.button) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dapp);
        ButterKnife.bind(this);
        setWebView();
        setWebViewHandlers();

    }

    private void setWebViewHandlers() {
    }

    private void setWebView() {

        DWebView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptObject(new JsApi(this), "");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(PermissionUtils.getPermissionsLocation());
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
//        webView.addJavascriptInterface();
        webView.loadUrl("http://192.168.88.230:8080");
//        webView.loadUrl("https://smartz.io/");
    }

    @OnClick(R.id.button)
    void onButtonClicked() {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("key", "value");
        webView.callHandler("test", new Object[]{stringStringHashMap}, new OnReturnValue<Object>() {
            @Override
            public void onValue(Object retValue) {
                Log.d(TAG, "onClick() called with: retValue = [" + retValue + "]");
            }
        });
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
        public void getAddress(Object abc, CompletionHandler handler) {
            handler.complete(AccountManager.getAddress().getHex());
        }

        @JavascriptInterface
        public void signTx(Object tx, CompletionHandler handler) {
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
    }
}
