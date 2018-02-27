package com.blockchain.store.playmarket.ui.exchange_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.MessageFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends AppCompatActivity implements ExchangeActivityContract.View {
    private static final String TAG = "ExchangeActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.user_address_field) EditText userAddressField;
    @BindView(R.id.entered_amount) EditText enteredAmount;
    @BindView(R.id.estimated_amount) TextView estimatedAmount;
    @BindView(R.id.exchange_icon) SimpleDraweeView exchangeIcon;
    @BindView(R.id.content_holder) LinearLayout contentHolder;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_holder) LinearLayout errorHolder;

    ExchangeActivityViewModel exchangeActivityViewModel;
    ExchangeActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        exchangeActivityViewModel = ViewModelProviders.of(this).get(ExchangeActivityViewModel.class);
        setViews();
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new ExchangeActivityPresenter();
        presenter.init(this);
        if (exchangeActivityViewModel.changellyCurrencies == null) {
            presenter.loadAllCurrencies();
        } else {
            initIcon(exchangeActivityViewModel.changellyCurrencies);
        }
        if (exchangeActivityViewModel.isErrorWithCurrency) {
            errorHolder.setVisibility(View.VISIBLE);
        } else {
            contentHolder.setVisibility(View.VISIBLE);
        }
    }

    private void setViews() {
        userAddressField.setText(AccountManager.getAddress().getHex());

    }

    @OnClick(R.id.get_estimated_amount_btn)
    public void onGetEstimatedAmountClicked() {
        presenter.getEstimatedAmount(
                exchangeActivityViewModel.changellyCurrencies.get(0).name,
                enteredAmount.getText().toString());
        // TODO
    }

    @OnClick(R.id.create_transaction)
    public void onCreateTransactionClicked() {
        presenter.createTransaction(exchangeActivityViewModel.changellyCurrencies.get(0).name, userAddressField.getText().toString(),
                enteredAmount.getText().toString(), null);
    }

    @OnClick(R.id.exchange_icon)
    void onExchangeIconClicked() {

    }

    @OnClick(R.id.top_layout_back_arrow)
    void onBackClicked() {
        this.onBackPressed();
    }

    @Override
    public void showLoadCurrenciesProgress(boolean isShown) {
        progressBar.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoadCurrenciesReady(ArrayList<ChangellyCurrency> currencies) {
        exchangeActivityViewModel.changellyCurrencies = currencies;
        exchangeActivityViewModel.isErrorWithCurrency = false;

        contentHolder.setVisibility(View.VISIBLE);
        errorHolder.setVisibility(View.GONE);
        initIcon(exchangeActivityViewModel.changellyCurrencies);
    }

    private void initIcon(ArrayList<ChangellyCurrency> changellyCurrencies) {
        exchangeIcon.setImageURI(Uri.parse(changellyCurrencies.get(0).getImageUrl()));
    }

    @Override
    public void onLoadCurrenciesFailed(Throwable throwable) {
        exchangeActivityViewModel.isErrorWithCurrency = true;
        contentHolder.setVisibility(View.GONE);
        errorHolder.setVisibility(View.VISIBLE);
        ToastUtil.showToast(throwable.getMessage());
    }

    @Override
    public void onEstimatedAmountReady(String result) {
        estimatedAmount.setText(result);
    }

    @Override
    public void onEstimatedAmountFail(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        presenter.loadAllCurrencies();
    }

}
