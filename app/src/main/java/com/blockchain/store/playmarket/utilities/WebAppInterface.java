package com.blockchain.store.playmarket.utilities;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context context;

    public WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void test2() {

    }

    @JavascriptInterface
    public void test() {

    }

    @JavascriptInterface
    public void unlockWalletMobile(String account, String pasword) {

    }
}
