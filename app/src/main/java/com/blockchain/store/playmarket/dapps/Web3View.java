package com.blockchain.store.playmarket.dapps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import wendu.dsbridge.DWebView;


public class Web3View extends DWebView {
    private JsInjectorClient jsInjectorClient;
    private Web3ViewClient webViewClient;
    private Web3ViewCallback callback;

    public Web3View(@NonNull Context context) {
        this(context, null);
    }

    public Web3View(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Web3View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init();
    }

    public void setCallback(Web3ViewCallback callback) {
        this.callback = callback;
    }


    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(client);

    }

    @androidx.annotation.Nullable
    @Nullable
    @Override
    public WebChromeClient getWebChromeClient() {
        return super.getWebChromeClient();
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(new WrapWebViewClient(webViewClient, client, jsInjectorClient));
    }

    @Override
    public Web3ViewClient getWebViewClient() {

        return webViewClient;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        jsInjectorClient = new JsInjectorClient(getContext());
        webViewClient = new Web3ViewClient(jsInjectorClient, new UrlHandlerManager());
        webViewClient.setCallback(callback);
        WebSettings webSettings = super.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);

        super.setWebViewClient(webViewClient);
    }

    public void setChainId(int chainId) {
        jsInjectorClient.setChainId(chainId);
    }

    public int getChainId() {
        return jsInjectorClient.getChainId();
    }

    public void setRpcUrl(@NonNull String rpcUrl) {
        jsInjectorClient.setRpcUrl(rpcUrl);
    }

    @Nullable
    public String getRpcUrl() {
        return jsInjectorClient.getRpcUrl();
    }

    private class WrapWebViewClient extends WebViewClient {
        private final Web3ViewClient internalClient;
        private final WebViewClient externalClient;
        private final JsInjectorClient jsInjectorClient;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (callback != null) {
                callback.onPageStarted(url);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (callback != null) {
                callback.onPageFinished(url);
            }
        }

        public WrapWebViewClient(Web3ViewClient internalClient, WebViewClient externalClient, JsInjectorClient jsInjectorClient) {
            this.internalClient = internalClient;
            this.externalClient = externalClient;
            this.jsInjectorClient = jsInjectorClient;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return externalClient.shouldOverrideUrlLoading(view, url)
                    || internalClient.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return externalClient.shouldOverrideUrlLoading(view, request)
                    || internalClient.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = externalClient.shouldInterceptRequest(view, request);
            if (response != null) {
                try {
                    InputStream in = response.getData();
                    int len = in.available();
                    byte[] data = new byte[len];
                    int readLen = in.read(data);
                    if (readLen == 0) {
                        throw new IOException("Nothing is read.");
                    }
                    String injectedHtml = jsInjectorClient.injectJS(new String(data));
                    response.setData(new ByteArrayInputStream(injectedHtml.getBytes()));
                } catch (IOException ex) {
                    Log.d("INJECT AFTER_EXTRNAL", "", ex);
                }
            } else {
                response = internalClient.shouldInterceptRequest(view, request);
            }
            return response;
        }
    }

    public interface Web3ViewCallback {
        void onPageStarted(String page);

        void onPageFinished(String page);
    }
}
