package com.blockchain.store.playmarket.ui.about_screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends AppCompatActivity {
    private static final String TAG = "AboutAppActivity";

    @BindView(R.id.version) TextView version;
    @BindView(R.id.our_site) TextView outSite;
    @BindView(R.id.browser_version) TextView browserVerstion;
    @BindView(R.id.organizational_structure) TextView organizational_structure;
    @BindView(R.id.investment_declaration) TextView investment_declaration;
    @BindView(R.id.terms_and_conditions) TextView terms_and_conditions;
    @BindView(R.id.privacy_policy) TextView privacy_policy;

    private String playMarketUrl = "https://ico.playmarket.io/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);

//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://ico.playmarket.io/conditions");
    }

    @OnClick(R.id.our_site)
    void onOurSiteClicked() {

    }

    @OnClick(R.id.browser_version)
    void onBrowserClicked() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(playMarketUrl));
        startActivity(i);
    }

    @OnClick(R.id.organizational_structure)
    void onStructureClicked() {

    }

    @OnClick(R.id.investment_declaration)
    void onDescalartionClicked() {

    }

    @OnClick(R.id.terms_and_conditions)
    void onTermsClicked() {

    }

    @OnClick(R.id.privacy_policy)
    void onPrivacyClicked() {

    }


}
