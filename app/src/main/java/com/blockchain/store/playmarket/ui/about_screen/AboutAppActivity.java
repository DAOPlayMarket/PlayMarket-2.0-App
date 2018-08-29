package com.blockchain.store.playmarket.ui.about_screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.ui.webview_screen.WebViewActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends AppCompatActivity {

    private static final String TAG = "AboutAppActivity";

    private static final String URL_PLAYMARKET = "https://ico.playmarket.io/";
    private static final String URL_PRIVACY = "https://ico.playmarket.io/privacy-policy";
    private static final String URL_CONDITIONS = "https://ico.playmarket.io/conditions";
    private static final String URL_INVEST_DECLARATION = "https://ico.playmarket.io/invest_declaration";
    private static final String URL_ORGANIZATIONAL_STRUCTURE = "https://ico.playmarket.io/org_legal_struct";


    @BindView(R.id.version) TextView version;
    @BindView(R.id.our_site) TextView outSite;
    @BindView(R.id.browser_version) TextView browserVerstion;
    @BindView(R.id.organizational_structure) TextView organizational_structure;
    @BindView(R.id.investment_declaration) TextView investment_declaration;
    @BindView(R.id.terms_and_conditions) TextView terms_and_conditions;
    @BindView(R.id.privacy_policy) TextView privacy_policy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        setVersion();
    }

    private void setVersion() {
        StringBuilder versionText = new StringBuilder();
        versionText.append("ver. ").append(BuildConfig.VERSION_NAME).append("\n");
        versionText.append("Node address: \n").append(RestApi.SERVER_ENDPOINT_WITHOUT_POST);
        version.setText(versionText);
    }

    @OnClick(R.id.our_site)
    void onOurSiteClicked() {
        WebViewActivity.start(this, URL_PLAYMARKET);
    }

    @OnClick(R.id.browser_version)
    void onBrowserClicked() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(URL_PLAYMARKET));
        startActivity(i);
    }

    @OnClick(R.id.organizational_structure)
    void onStructureClicked() {
        WebViewActivity.start(this, URL_ORGANIZATIONAL_STRUCTURE);
    }

    @OnClick(R.id.investment_declaration)
    void onDeclarationClicked() {
        WebViewActivity.start(this, URL_INVEST_DECLARATION);
    }

    @OnClick(R.id.terms_and_conditions)
    void onTermsClicked() {
        WebViewActivity.start(this, URL_CONDITIONS);
    }

    @OnClick(R.id.privacy_policy)
    void onPrivacyClicked() {
        WebViewActivity.start(this, URL_PRIVACY);
    }


}
