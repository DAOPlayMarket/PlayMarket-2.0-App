package com.blockchain.store.playmarket.ui.pax_screen;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.device.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PexActivity extends AppCompatActivity {
    private static final String TAG = "PexActivity";
    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pex_activity);
        ButterKnife.bind(this);
        setUpWebView();
    }

    @SuppressLint("JavascriptInterface")
    private void setUpWebView() {
        webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG, "onConsoleMessage() called with: consoleMessage = [" + consoleMessage.message() + "]");
                return super.onConsoleMessage(consoleMessage);
            }

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
                webView.loadUrl("javascript:unlockWalletMobile({\"address\":\"9e1f601d72bda509d82ed7082d9d3a7e0f4d012b\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"ec6ef96cad3b373817187222cfe6a3d030fe895de92cf6b454a4b56231ceb08a\",\"cipherparams\":{\"iv\":\"b6047fc897dd5a5558b70ff9fe0ab77a\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"bed366309858361441599874817a2835adf4d379539803a37148caac1830ab3c\"},\"mac\":\"5a5f317a74f06c5ed2167f4752bffcfc073da55a19918f3e03bc17e987860cc4\"},\"id\":\"a18a8ca5-b321-467b-88a1-d369223f4a07\",\"version\":3},123123123)");
//                webView.evaluateJavascript("unlockWalletMobile({\"address\":\"9e1f601d72bda509d82ed7082d9d3a7e0f4d012b\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"ec6ef96cad3b373817187222cfe6a3d030fe895de92cf6b454a4b56231ceb08a\",\"cipherparams\":{\"iv\":\"b6047fc897dd5a5558b70ff9fe0ab77a\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"bed366309858361441599874817a2835adf4d379539803a37148caac1830ab3c\"},\"mac\":\"5a5f317a74f06c5ed2167f4752bffcfc073da55a19918f3e03bc17e987860cc4\"},\"id\":\"a18a8ca5-b321-467b-88a1-d369223f4a07\",\"version\":3},123123123)", null);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(Constants.PAX_URL);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
