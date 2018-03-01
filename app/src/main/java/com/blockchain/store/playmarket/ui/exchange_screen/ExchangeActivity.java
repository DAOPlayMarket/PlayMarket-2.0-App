package com.blockchain.store.playmarket.ui.exchange_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ChangellyCurrenciesAdapter;
import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.fragment_dialogs.ChooseCurrencyDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeActivity extends AppCompatActivity implements ExchangeActivityContract.View, ChangellyCurrenciesAdapter.ChangellyAdapterCallback {
    private static final String TAG = "ExchangeActivity";

    @BindView(R.id.top_layout_app_name) TextView toolbarTitle;
    @BindView(R.id.user_address_field) EditText userAddressField;
    @BindView(R.id.entered_amount) EditText enteredAmount;
    @BindView(R.id.estimated_amount) TextView estimatedAmount;
    @BindView(R.id.exchange_icon) SimpleDraweeView exchangeIcon;
    @BindView(R.id.content_holder) LinearLayout contentHolder;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.payin_address) TextView payinAddress;

    ExchangeActivityViewModel exchangeActivityViewModel;
    ExchangeActivityPresenter presenter;
    ChooseCurrencyDialog exchangeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        exchangeActivityViewModel = ViewModelProviders.of(this).get(ExchangeActivityViewModel.class);
        attachPresenter();
    }

    private void attachPresenter() {
        presenter = new ExchangeActivityPresenter();
        presenter.init(this);
        if (exchangeActivityViewModel.changellyCurrencies == null) {
            presenter.loadAllCurrencies();
        } else {
            onLoadCurrenciesReady(exchangeActivityViewModel.changellyCurrencies);
            contentHolder.setVisibility(View.VISIBLE);
        }
    }

    private void setViews(ArrayList<ChangellyCurrency> currencies) {
        exchangeDialog = (ChooseCurrencyDialog) getSupportFragmentManager().findFragmentByTag("dialog_tag");
        if (exchangeDialog == null)
            exchangeDialog = ChooseCurrencyDialog.instance(currencies);
        if (currencies.size() > 0)
            initIcon(currencies.get(0));
        userAddressField.setText(AccountManager.getAddress().getHex());

    }

    @OnClick(R.id.get_estimated_amount_btn)
    public void onGetEstimatedAmountClicked() {
        presenter.getEstimatedAmount(
                exchangeActivityViewModel.chosenCurrency.name,
                enteredAmount.getText().toString());
    }

    @OnClick(R.id.create_transaction)
    public void onCreateTransactionClicked() {
        presenter.createTransaction(exchangeActivityViewModel.changellyCurrencies.get(0).name, userAddressField.getText().toString(),
                enteredAmount.getText().toString(), null);
    }

    @OnClick(R.id.exchange_holder)
    void onExchangeIconClicked() {
        exchangeDialog.show(getSupportFragmentManager(), "dialog_tag");
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
        contentHolder.setVisibility(View.VISIBLE);
        errorHolder.setVisibility(View.GONE);
        setViews(currencies);
    }

    private void initIcon(ChangellyCurrency changellyCurrencies) {
        exchangeActivityViewModel.chosenCurrency = changellyCurrencies;
        exchangeIcon.setImageURI(Uri.parse(changellyCurrencies.getImageUrl()));
    }

    @Override
    public void onLoadCurrenciesFailed(Throwable throwable) {
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

    @Override
    public void onTransactionCreatedSuccessfully(ChangellyCreateTransactionResponse changellyCreateTransactionResponse) {
        payinAddress.setText(changellyCreateTransactionResponse.result.payinAddress);
    }

    @Override
    public void onTransactionCreatedFailed(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
        presenter.loadAllCurrencies();
    }

    @Override
    public void onChangellyCurrencyClicked(ChangellyCurrency changellyCurrency) {
        initIcon(changellyCurrency);
        exchangeDialog.dismissAllowingStateLoss();
    }

}
