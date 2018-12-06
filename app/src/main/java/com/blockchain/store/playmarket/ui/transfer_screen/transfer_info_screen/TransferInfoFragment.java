package com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class TransferInfoFragment extends Fragment implements TransferInfoContract.View {

    private static final String ETH = "ETH";
    private static final String WEI = "WEI";

    @BindView(R.id.sender_address_textView) TextView senderAddressTextView;
    @BindView(R.id.recipient_address_editText) EditText recipientAddressEditText;
    @BindView(R.id.amount_editText) EditText amountEditText;
    @BindView(R.id.balance_textView) TextView balanceTextView;
    @BindView(R.id.sender_address_info_textView) TextView senderAddressInfoTextView;
    @BindView(R.id.recipient_address_info_textView) TextView recipientAddressInfoTextView;
    @BindView(R.id.dimension_info_textView) TextView dimensionInfoTextView;
    @BindView(R.id.amount_info_textView) TextView amountInfoTextView;
    @BindView(R.id.wei_textView) TextView weiTextView;
    @BindView(R.id.eth_textView) TextView ethTextView;
    @BindView(R.id.custom_token) TextView customTokenTextView;
    @BindView(R.id.error_view_holder) LinearLayout errorViewHolder;
    @BindView(R.id.recipient_address_textInputLayout) TextInputLayout recipientAddressTextInputLayout;
    @BindView(R.id.amount_textInputLayout) TextInputLayout amountTextInputLayout;
    @BindView(R.id.qr_scanner_button) ImageButton qrCodeImage;
    @BindView(R.id.dimension_linearLayout) View dimensionHolder;
    @BindView(R.id.dimension_textView) TextView dimensionView;
    @BindView(R.id.balance_in_local) TextView balanceInLocalCurrency;

    private TransferInfoPresenter presenter;
    private String recipientAddress;
    private String transferAmount;
    private BigDecimal accountBalanceInEther;
    private TransferViewModel transferViewModel;
    private boolean isEth;
    private String tokenName;
    private Constants.TransactionTypes transactionTypes;
    private UserBalance accountBalance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_info, container, false);
        ButterKnife.bind(this, view);

        transferViewModel = ViewModelProviders.of(getActivity()).get(TransferViewModel.class);

        presenter = new TransferInfoPresenter();
        presenter.init(this, getContext());
        if (transferViewModel.totalBalance.getValue() != null) {
            balanceTextView.setText(transferViewModel.totalBalance.getValue());
        } else {
            presenter.getAccountBalance();
        }

        String senderAddress = presenter.getSenderAddress();
        senderAddressTextView.setText(senderAddress);
        transferViewModel.senderAddress.setValue(senderAddressTextView.getText().toString());

        getDataFromTransferViewModel();

        if (recipientAddress == null) {
            recipientAddressEditText.setEnabled(true);
        } else {
            recipientAddressEditText.setText(recipientAddress);
            recipientAddressEditText.setEnabled(false);
            qrCodeImage.setEnabled(false);
        }
        if (transferAmount != null) {
            amountEditText.setText(transferAmount);
        }
        if (transferViewModel.isBlockEthIcon != null && transferViewModel.isBlockEthIcon.getValue()) {
            dimensionHolder.setEnabled(false);
            amountEditText.setEnabled(false);
            ethSelect();
        }

        if (tokenName != null) {
            customTokenSelect();
        } else {
            ethSelect();
        }

        return view;
    }


    private void setTransactionType(Constants.TransactionTypes transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    @OnClick(R.id.dimension_linearLayout)
    void dimensionClicked() {
        if (isEth) weiSelect();
        else ethSelect();
    }

    @OnTextChanged(value = R.id.recipient_address_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onRecipientAddressChanged(Editable editable) {
        recipientAddressInfoTextView.setText(editable.toString());
        transferViewModel.recipientAddress.setValue(editable.toString());
    }

    @OnTextChanged(value = R.id.amount_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onAmountChanged(Editable editable) {
        amountInfoTextView.setText(editable);
        transferViewModel.transferAmount.setValue(editable.toString());
    }

    @OnClick(R.id.qr_scanner_button)
    void scanButtonPressed() {
        Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        recipientAddressEditText.setText(data.getStringExtra("qrResult"));
    }

    @Override
    public void getAccountBalanceSuccessful(UserBalance accountBalance) {
        this.accountBalance = accountBalance;
        errorViewHolder.setVisibility(View.GONE);
        accountBalanceInEther = new EthereumPrice(accountBalance.balanceInWei).inEther();
        balanceTextView.setText(accountBalanceInEther.toString());
        transferViewModel.balance.setValue(balanceTextView.getText().toString());
        balanceInLocalCurrency.setText(String.format(getString(R.string.local_currency), accountBalance.symbol, accountBalance.getFormattedLocalCurrency()));
    }

    @Override
    public void onAccountBalanceError(Throwable throwable) {
        errorViewHolder.setVisibility(View.VISIBLE);
    }

    private void setTokenName(String tokenName) {
        this.tokenName = tokenName;
        dimensionView.setText(tokenName);

    }

    private void customTokenSelect() {
        dimensionHolder.setClickable(false);

        dimensionView.setText(tokenName);
        dimensionInfoTextView.setText(tokenName);
        transferViewModel.dimension.setValue(tokenName);
        customTokenTextView.setText(tokenName);

        customTokenTextView.setVisibility(View.VISIBLE);
        customTokenTextView.setBackgroundResource(R.drawable.round_corner_green_button);
        customTokenTextView.setTextColor(getResources().getColor(R.color.white));


        ethTextView.setVisibility(View.GONE);
        weiTextView.setVisibility(View.GONE);

    }

    private void ethSelect() {
        ethTextView.setBackgroundResource(R.drawable.round_corner_green_button);
        ethTextView.setTextColor(getResources().getColor(R.color.white));

        weiTextView.setBackgroundResource(R.color.Clear);
        weiTextView.setTextColor(getResources().getColor(R.color.colorAccent));

        dimensionInfoTextView.setText(ETH);

        isEth = true;
        transferViewModel.isEth.setValue(isEth);
        transferViewModel.dimension.setValue(ETH);
    }

    private void weiSelect() {
        weiTextView.setBackgroundResource(R.drawable.round_corner_green_button);
        weiTextView.setTextColor(getResources().getColor(R.color.white));

        ethTextView.setBackgroundResource(R.color.Clear);
        weiTextView.setTextColor(getResources().getColor(R.color.colorAccent));

        dimensionInfoTextView.setText(WEI);

        isEth = false;
        transferViewModel.isEth.setValue(isEth);
        transferViewModel.dimension.setValue(WEI);
    }

    public boolean isHasNoError() {
        if (errorViewHolder.getVisibility() == View.VISIBLE) return false;

        boolean emptyAmountCheck = true;
        boolean isHasNoError = true;

        if (recipientAddressEditText.getText().length() == 0) {
            recipientAddressTextInputLayout.setError(getResources().getString(R.string.empty_field));
            isHasNoError = false;
        } else if (recipientAddressEditText.getText().length() > 1 && recipientAddressEditText.getText().length() < 42) {
            recipientAddressTextInputLayout.setError(getResources().getString(R.string.short_account));
            isHasNoError = false;
        } else recipientAddressTextInputLayout.setError("");

        if (amountEditText.getText().toString().isEmpty() && !amountEditText.getText().toString().equalsIgnoreCase("0")) {
            amountTextInputLayout.setError(getResources().getString(R.string.empty_field));
            emptyAmountCheck = false;
            isHasNoError = false;
        } else amountTextInputLayout.setError("");

        if (tokenName != null) {
            String transferAmount = amountEditText.getText().toString();
            String totalBalance = balanceTextView.getText().toString();
            if (transactionTypes == Constants.TransactionTypes.BUY_APP) {
                totalBalance = accountBalance.balanceInLocalCurrency;
            }
            if (!totalBalance.isEmpty() & Double.parseDouble(transferAmount) < Double.parseDouble(totalBalance)) {
                return true;
            } else {
                amountTextInputLayout.setError(getResources().getString(R.string.insufficient_funds));
                return false;
            }
        } else if (emptyAmountCheck) {
            String transferAmount = amountEditText.getText().toString();
            String balanceAmountInEther = accountBalanceInEther.toString();

            transferAmount = new EthereumPrice(transferAmount, isEth ? EthereumPrice.Currency.ETHER : EthereumPrice.Currency.WEI).inLongToString();
            balanceAmountInEther = new EthereumPrice(balanceAmountInEther, EthereumPrice.Currency.ETHER).inLongToString();

            BigDecimal transferAmountBigDecimal = new BigDecimal(transferAmount);
            BigDecimal balanceBigDecimal = new BigDecimal(balanceAmountInEther);
            if (transferAmountBigDecimal.doubleValue() > balanceBigDecimal.doubleValue()) {
                amountTextInputLayout.setError(getResources().getString(R.string.insufficient_funds));
                isHasNoError = false;
            } else amountTextInputLayout.setError("");
        }
        return isHasNoError;
    }


    @OnClick(R.id.error_view_repeat_btn)
    void onErrorViewRepeatClicked() {
        errorViewHolder.setVisibility(View.GONE);
        presenter.getAccountBalance();
    }

    private void getDataFromTransferViewModel() {
        transferViewModel.recipientAddress.observe(getActivity(), s -> recipientAddress = s);
        transferViewModel.transferAmount.observe(getActivity(), s -> transferAmount = s);
        transferViewModel.tokenName.observe(getActivity(), this::setTokenName);
        transferViewModel.transactionType.observe(getActivity(), this::setTransactionType);
    }

}
