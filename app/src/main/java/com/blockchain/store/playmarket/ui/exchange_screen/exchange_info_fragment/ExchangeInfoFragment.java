package com.blockchain.store.playmarket.ui.exchange_screen.exchange_info_fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.ChangellyCurrenciesAdapter;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.ui.exchange_screen.ExchangeActivityViewModel;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.fragment_dialogs.ChooseCurrencyDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeInfoFragment extends Fragment implements ExchangeInfoFragmentContract.View, ChangellyCurrenciesAdapter.ChangellyAdapterCallback {
    private static final String TAG = "ExchangeInfoFragment";
    private static final String DIALOG_TAG = "dialog_tag";
    private static final int DEBOUNCE_INTERVAL_MILLIS = 1000;

    @BindView(R.id.user_address_field) EditText userAddressField;
    @BindView(R.id.entered_amount) EditText enteredAmount;
    @BindView(R.id.exchange_icon) SimpleDraweeView exchangeIcon;
    @BindView(R.id.content_holder) LinearLayout contentHolder;
    @BindView(R.id.progress_holder) LinearLayout progressLayout;
    @BindView(R.id.error_holder) LinearLayout errorHolder;
    @BindView(R.id.minimum_amount) TextView minimumAmount;
    @BindView(R.id.layout_holder) View layoutHolder;

    @BindView(R.id.chosen_currency_name) TextView chosenCurrencyName;
    @BindView(R.id.chosen_currency_fullname) TextView chosenCurrencyFullname;
    @BindView(R.id.exchange_details_fee) TextView exchangeDetailsFee;
    @BindView(R.id.exchange_details_currency) TextView exchangeDetailsCurrency;
    @BindView(R.id.exchange_details_amount) TextView exchangeDetailsAmount;

    private ExchangeActivityViewModel exchangeActivityViewModel;
    private ExchangeInfoFragmentPresenter presenter;
    private ChooseCurrencyDialog exchangeDialog;
    private BehaviorSubject<String> userInputSubject = BehaviorSubject.create();

    public ExchangeInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_info, container, false);
        ButterKnife.bind(this, view);
        exchangeActivityViewModel = ViewModelProviders.of(getActivity()).get(ExchangeActivityViewModel.class);
        attachPresenter();
        initViews();
        initCurrencyDialog();

        return view;
    }

    @OnTextChanged(value = R.id.entered_amount, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAmountChanged(Editable editable) {
        String enteredAmount = editable.toString();
        exchangeActivityViewModel.userEnteredAmount.setValue(enteredAmount);
        if (!enteredAmount.isEmpty()) {
            userInputSubject.onNext(enteredAmount);
        }
    }

    private void attachPresenter() {
        presenter = new ExchangeInfoFragmentPresenter();
        presenter.init(this);
        contentHolder.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        onCurrencyChosen(exchangeActivityViewModel.chosenCurrency.getValue());
        onMinimumAmountReady(exchangeActivityViewModel.minEnteredAmount.getValue());
        userAddressField.setText(AccountManager.getAddress().getHex());
        userInputSubject.debounce(DEBOUNCE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(text -> !text.isEmpty())
                .subscribe(text -> {
                    enteredAmount.setError(null);
                    exchangeDetailsAmount.setVisibility(View.GONE);
                    presenter.getEstimatedAmount(
                            exchangeActivityViewModel.chosenCurrency.getValue().name.toUpperCase(),
                            text);
                });
    }


    private void initCurrencyDialog() {
        exchangeDialog = (ChooseCurrencyDialog) getChildFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (exchangeDialog == null)
            exchangeDialog = ChooseCurrencyDialog.instance(exchangeActivityViewModel.changellyCurrencies.getValue());
    }

    @OnClick(R.id.exchange_holder)
    void onExchangeIconClicked() {
        exchangeDialog.show(getChildFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void showLoadCurrenciesProgress(boolean isShown) {
        progressLayout.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onMinimumAmountReady(String amount) {
        layoutHolder.setVisibility(View.VISIBLE);
        exchangeActivityViewModel.minEnteredAmount.setValue(amount);
        this.minimumAmount.setText(String.format(getString(R.string.send_amount), amount));
    }

    private void onCurrencyChosen(ChangellyCurrency changellyCurrencies) {
        exchangeActivityViewModel.chosenCurrency.setValue(changellyCurrencies);
        exchangeIcon.setImageURI(Uri.parse(changellyCurrencies.getImageUrl()));
        chosenCurrencyName.setText(exchangeActivityViewModel.chosenCurrency.getValue().name.toUpperCase());
        chosenCurrencyFullname.setText(exchangeActivityViewModel.chosenCurrency.getValue().fullName);
        exchangeDetailsCurrency.setText(String.format(getString(R.string.exchange_details_currency), changellyCurrencies.name.toUpperCase()));
    }


    @Override
    public void onEstimatedAmountReady(String estimatedAmount) {
        exchangeActivityViewModel.estimatedAmount.setValue(estimatedAmount);
        exchangeDetailsAmount.setVisibility(View.VISIBLE);
        exchangeDetailsFee.setVisibility(View.VISIBLE);
        exchangeDetailsAmount.setText(String.format(getString(R.string.exchange_details_amount),
                enteredAmount.getText().toString(),
                exchangeActivityViewModel.chosenCurrency.getValue().name,
                estimatedAmount,
                getString(R.string.ethereum_name)));
    }

    @Override
    public void onEstimatedAmountFail(Throwable throwable) {
        ToastUtil.showToast(throwable.getMessage());
    }

    @Override
    public void onMinimumAmountError(Throwable throwable) {
        ToastUtil.showToast("Error while getting minimum amount. Please choose another currency to exchange. " + throwable.getMessage());
        layoutHolder.setVisibility(View.GONE);
    }

    @OnClick(R.id.error_view_repeat_btn)
    public void error_view_repeat_btn() {
    }

    @Override
    public void onChangellyCurrencyClicked(ChangellyCurrency changellyCurrency) {
        onCurrencyChosen(changellyCurrency);
        enteredAmount.setText(enteredAmount.getText());
        exchangeDialog.dismissAllowingStateLoss();
        presenter.loadMinimumAmount(changellyCurrency);
    }

    public boolean isCanCreateTransaction() {
        if (enteredAmount.getText().toString().trim().isEmpty() ||
                exchangeActivityViewModel.userEnteredAmount.getValue() == null ||
                Double.parseDouble(exchangeActivityViewModel.userEnteredAmount.getValue()) <
                        Double.parseDouble(exchangeActivityViewModel.minEnteredAmount.getValue())) {
            enteredAmount.setError(getString(R.string.below_minimum_amount_error));
            enteredAmount.requestFocus();
            return false;
        }
        return true;
    }
}
