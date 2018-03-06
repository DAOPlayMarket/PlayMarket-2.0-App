package com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferInfoFragment extends Fragment implements TransferInfoContract.View {

    private TransferInfoPresenter presenter;

    private BigDecimal balance;

    private TransferViewModel transferViewModel;

    @BindView(R.id.sender_address_editText)
    EditText senderAddressEditText;

    @BindView(R.id.recipient_address_editText)
    EditText recipientAddressEditText;

    @BindView(R.id.amount_editText)
    EditText amountEditText;

    @BindView(R.id.recipient_address_inputLayout)
    TextInputLayout recipientAddressInputLayout;

    @BindView(R.id.amount_inputLayout)
    TextInputLayout amountInputLayout;

    @BindView(R.id.balance_textView)
    TextView balanceTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_info, container, false);
        ButterKnife.bind(this, view);

        transferViewModel = ViewModelProviders.of(getParentFragment()).get(TransferViewModel.class);

        presenter = new TransferInfoPresenter();
        presenter.init(this, getContext());
        presenter.getAccountBalance();
        senderAddressEditText.setText(presenter.getSenderAddress());

        return view;
    }

    @OnClick(R.id.continue_transfer_button)
    void continueButtonPressed() {

        boolean emptyAddressCheck = true;
        boolean emptyAmountCheck = true;
        boolean amountCountCheck = true;

        if (recipientAddressEditText.getText().length() == 0) {
            recipientAddressInputLayout.setErrorEnabled(true);
            recipientAddressInputLayout.setError(getResources().getString(R.string.empty_field));
            emptyAddressCheck = false;
        }
        //else if (recipientAddressEditText.getText().length() > 1 && recipientAddressEditText.getText().length() < 42) {
        //    recipientAddressInputLayout.setErrorEnabled(true);
        //    recipientAddressInputLayout.setError(getResources().getString(R.string.short_account));
        //    emptyAddressCheck = false;
        //}
        else recipientAddressInputLayout.setErrorEnabled(false);

        if (amountEditText.getText().length() == 0) {
            amountInputLayout.setErrorEnabled(true);
            amountInputLayout.setError(getResources().getString(R.string.empty_field));
            emptyAmountCheck = false;
        } else amountInputLayout.setErrorEnabled(false);

        if (emptyAmountCheck) {
            BigDecimal transferAmount = new BigDecimal(amountEditText.getText().toString());
            if (transferAmount.doubleValue() > balance.doubleValue()) {
                amountInputLayout.setErrorEnabled(true);
                amountInputLayout.setError(getResources().getString(R.string.insufficient_funds));
                amountCountCheck = false;
            } else amountInputLayout.setErrorEnabled(false);

        }

        if (emptyAddressCheck && emptyAmountCheck && amountCountCheck) {
            putDataOnViewModel();
            ((TransferFragment) getParentFragment()).goToConfirmTransfer();
        }
    }

    @Override
    public void getAccountBalanceSuccessful(String accountBalance) {
        balance = new EthereumPrice(accountBalance).inEther();
        balanceTextView.setText(balance.toString());

    }

    private void putDataOnViewModel() {
        transferViewModel.senderAddress.setValue(senderAddressEditText.getText().toString());
        transferViewModel.recipientAddress.setValue(recipientAddressEditText.getText().toString());
        transferViewModel.transferAmount.setValue(amountEditText.getText().toString());
        transferViewModel.balance.setValue(balanceTextView.getText().toString());
    }
}
