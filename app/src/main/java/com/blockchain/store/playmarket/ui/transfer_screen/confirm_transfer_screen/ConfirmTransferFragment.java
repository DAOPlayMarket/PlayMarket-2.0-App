package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmTransferFragment extends Fragment implements ConfirmTransferContract.View {

    private ConfirmTransferPresenter presenter;

    private TransferViewModel transferViewModel;

    private Observer<String> senderAddressObserver;
    private Observer<String> recipientAddressObserver;
    private Observer<String> transferAmountObserver;
    private Observer<String> balanceObserver;

    @BindView(R.id.sender_address_textView)
    TextView senderAddressTextView;
    @BindView(R.id.recipient_address_textView)
    TextView recipientAddressTextView;
    @BindView(R.id.transfer_amount_textView)
    TextView transferAmountTextView;
    @BindView(R.id.sender_password_editText)
    EditText senderPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_transfer, container, false);
        ButterKnife.bind(this, view);

        transferViewModel = ViewModelProviders.of(getParentFragment()).get(TransferViewModel.class);

        senderAddressObserver = senderAddressValue -> senderAddressTextView.setText(senderAddressValue);
        recipientAddressObserver = recipientAddressValue -> recipientAddressTextView.setText(recipientAddressValue);
        transferAmountObserver = transferAmountValue -> transferAmountTextView.setText(transferAmountValue);

        transferViewModel.senderAddress.observeForever(senderAddressObserver);
        transferViewModel.recipientAddress.observeForever(recipientAddressObserver);
        transferViewModel.transferAmount.observeForever(transferAmountObserver);

        presenter = new ConfirmTransferPresenter();
        presenter.init(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        transferViewModel.senderAddress.removeObserver(senderAddressObserver);
        transferViewModel.recipientAddress.removeObserver(recipientAddressObserver);
        transferViewModel.transferAmount.removeObserver(transferAmountObserver);

    }

    @OnClick(R.id.back_button)
    void backButtonPressed() {
        ((TransferFragment) getParentFragment()).goToTransferInfo();
    }

    @OnClick(R.id.confirm_transfer_button)
    void confirmButtonPressed() {
        String password = senderPasswordEditText.getText().toString();
        if (presenter.passwordCheck(password)){
            String transferAmount = transferAmountTextView.getText().toString();
            String recipientAddress = recipientAddressTextView.getText().toString();
            String transferAmountInEth = new EthereumPrice(transferAmount, EthereumPrice.Currency.ETHER).inWeiString();
            presenter.createTransaction(transferAmountInEth, recipientAddress);
        }
    }
}
