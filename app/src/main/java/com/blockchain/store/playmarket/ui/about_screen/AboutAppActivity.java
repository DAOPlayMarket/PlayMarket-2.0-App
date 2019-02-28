package com.blockchain.store.playmarket.ui.about_screen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.ui.webview_screen.WebViewActivity;
import com.blockchain.store.playmarket.utilities.BaseActivity;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends BaseActivity {

    private static final String TAG = "AboutAppActivity";

    private static final String URL_PLAYMARKET = "http://playmarket.io/";
    private static final String URL_PRIVACY = "https://ico.playmarket.io/privacy-policy";
    private static final String URL_CONDITIONS = "https://ico.playmarket.io/conditions";
    private static final String URL_INVEST_DECLARATION = "https://ico.playmarket.io/invest_declaration";
    private static final String URL_ORGANIZATIONAL_STRUCTURE = "https://ico.playmarket.io/org_legal_struct";

    @BindView(R.id.version) TextView version;
    @BindView(R.id.about_text) TextView aboutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
        initFields();
        setVersion();
    }

    private void initFields() {
        aboutText.setText(String.format(getString(R.string.about_playmarket_2_0), Calendar.getInstance().get(Calendar.YEAR)));
    }

    private void setVersion() {
        String serverEndpoint = Hawk.get(Constants.LAST_KNOWN_NODE_ADDRESS, "");
        StringBuilder versionText = new StringBuilder();
        versionText.append("ver. ").append(BuildConfig.VERSION_NAME).append("\n");
        versionText.append("Node address: \n").append(serverEndpoint);
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
