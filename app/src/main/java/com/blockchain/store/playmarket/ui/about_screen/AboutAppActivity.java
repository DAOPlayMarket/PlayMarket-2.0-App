package com.blockchain.store.playmarket.ui.about_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutAppActivity extends AppCompatActivity {
    private static final String TAG = "AboutAppActivity";

    @BindView(R.id.webView) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://ico.playmarket.io/conditions");
    }

}
