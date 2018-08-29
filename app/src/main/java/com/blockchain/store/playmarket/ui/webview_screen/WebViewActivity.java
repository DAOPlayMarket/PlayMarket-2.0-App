package com.blockchain.store.playmarket.ui.webview_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private static final String EXTRA_ADDRESS_ARG = "address_extra";

    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private String urlAddress;

    public static void start(Context context, String urlAddress) {
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(EXTRA_ADDRESS_ARG, urlAddress);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            urlAddress = getIntent().getStringExtra(EXTRA_ADDRESS_ARG);
        } else {
            this.finish();
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(urlAddress);
    }

}
