package com.blockchain.store.playmarket.ui.transfer_screen.transfer_confirm_screen;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class TransferConfirmFragment extends Fragment {


    private TransferViewModel transferViewModel;

    @BindView(R.id.sender_address_info_textView)
    TextView senderAddressTextView;

    @BindView(R.id.recipient_address_info_textView)
    TextView recipientAddressTextView;

    @BindView(R.id.amount_info_textView)
    TextView transferAmountTextView;

    @BindView(R.id.dimension_textView)
    TextView dimensionTextView;

    @BindView(R.id.password_editText)
    EditText senderPasswordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_confirm, container, false);
        ButterKnife.bind(this, view);
        getDataFromViewModel();

        return view;
    }

    @OnTextChanged(value = R.id.password_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onPasswordChanged(Editable editable) {
        transferViewModel.senderPassword.setValue(editable.toString());
    }

    private void getDataFromViewModel() {
        transferViewModel = ViewModelProviders.of(getActivity()).get(TransferViewModel.class);
        transferViewModel.senderAddress.observe(getActivity(), s -> senderAddressTextView.setText(s));
        transferViewModel.recipientAddress.observe(getActivity(), s -> recipientAddressTextView.setText(s));
        transferViewModel.transferAmount.observe(getActivity(), s -> transferAmountTextView.setText(s));
        transferViewModel.dimension.observe(getActivity(), s -> dimensionTextView.setText(s));
    }

    public void showError(){
        senderPasswordEditText.setError(getResources().getString(R.string.insufficient_funds));
        senderPasswordEditText.requestFocus();
    }
}
