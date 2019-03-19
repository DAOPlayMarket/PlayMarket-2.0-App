package com.blockchain.store.playmarket.ui.pex_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.dapps.Web3View;
import com.blockchain.store.playmarket.data.entities.DappTransaction;
import com.blockchain.store.playmarket.interfaces.BackPressedCallback;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;
import com.blockchain.store.playmarket.utilities.dialogs.DappTxDialog;
import com.google.gson.Gson;

import org.ethereum.geth.Transaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wendu.dsbridge.CompletionHandler;

import static com.blockchain.store.playmarket.api.RestApi.BASE_URL_INFURA;


public class DappsFragment extends Fragment implements BackPressedCallback, DappTxDialog.TxDialogCallback {
    private static final String TAG = "DappsFragment";
    private static final String IS_OPEN_FOR_DEX_KEY = "is_open_for_dex";

    @BindView(R.id.web_view) Web3View webView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.browser_top_layout) LinearLayout topLayout;
    @BindView(R.id.webview_url_field) EditText urlField;
    @BindView(R.id.webview_home_field) TextView homeField;

    private boolean isOpenForDex = false;
    private Web3j web3j = Web3jFactory.build(new HttpService(BASE_URL_INFURA));
    private boolean isUserSawThisPage = false;
    private boolean isWebViewAlreadyInit = false;

    public static DappsFragment newInstance() {
        Bundle args = new Bundle();
        DappsFragment fragment = new DappsFragment();
        args.putBoolean(IS_OPEN_FOR_DEX_KEY, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dapps, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            isOpenForDex = getArguments().getBoolean(IS_OPEN_FOR_DEX_KEY, false);
            topLayout.setVisibility(View.GONE);
        }
        initEdittext();
        setWebView();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isUserSawThisPage) {
            this.isUserSawThisPage = true;

        }
    }

    private void initEdittext() {
        urlField.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String url = urlField.getText().toString();
                if (!urlField.getText().toString().startsWith("http")) {
                    url = "https://" + url;
                }
                if (!url.trim().isEmpty()) {
                    webView.loadUrl(url);
                }
                return true;
            }
            return false;
        });
    }

    private void setWebView() {
        isWebViewAlreadyInit = true;
        webView.addJavascriptObject(new JsApi(getActivity()), "");
        webView.setCallback(new Web3View.Web3ViewCallback() {
            @Override
            public void onPageStarted(String page) {
                urlField.setText(page);
            }

            @Override
            public void onPageFinished(String page) {
            }
        });
        loadDefaultUrl();

    }

    private void loadDefaultUrl() {
        if (isOpenForDex) {
            webView.loadUrl(Constants.PAX_URL);

        } else {
            webView.loadUrl("https://dapps.playmarket.io/");
        }
    }

    @OnClick(R.id.webview_home_field)
    void onHomeClicked() {
        webView.clearHistory();
        loadDefaultUrl();
    }

    @Override
    public boolean isUserCanHandleBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }

        return false;
    }


    @Override
    public void onAccountUnlocked(DappTransaction dappTransaction) {
        try {
            Transaction dapTx = dappTransaction.createTx();
            sendRawTransaction(dapTx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class JsApi {
        private Context context;

        public JsApi(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void getAccounts(Object abc, CompletionHandler handler) {
            Log.d(TAG, "getAccounts() called with: handler = [" + AccountManager.getAccount().getAddress().getHex() + "]" + abc);
            handler.complete(AccountManager.getAccount().getAddress().getHex());
        }


        @JavascriptInterface
        public void signTx(Object tx, CompletionHandler handler) {
            Log.d(TAG, "signTx() called with: tx = [" + tx + "], handler = [" + handler + "]");
            handler.complete();
        }


        @JavascriptInterface
        public void sendTransaction(Object tx, CompletionHandler handler) {
            Log.d(TAG, "sendTransaction() called with: tx = [" + tx + "], handler = [" + handler + "]");
            DappTransaction dappTransaction = new Gson().fromJson(tx.toString(), DappTransaction.class);
            DappTxDialog.newInstance(dappTransaction).show(getChildFragmentManager(), "");

        }
    }

    private void sendRawTransaction(Transaction tx) {
        web3j.ethSendRawTransaction("0x" + CryptoUtils.getRawTransaction(tx)).observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTxSend, this::onTxFailed);
    }

    private void onTxSend(EthSendTransaction ethSendTransaction) {
    }

    private void onTxFailed(Throwable throwable) {
    }

}
