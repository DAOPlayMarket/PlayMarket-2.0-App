package com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferViewModel;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen.TransferInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmTransferFragment extends Fragment implements ConfirmTransferContract.View {

    private ConfirmTransferPresenter presenter;

    private TransferViewModel transferViewModel;

    @BindView(R.id.sender_address_textView) TextView senderAddressTextView;
    @BindView(R.id.recipient_address_textView) TextView recipientAddressTextView;
    @BindView(R.id.transfer_amount_textView) TextView transferAmountTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_transfer, container, false);
        ButterKnife.bind(this, view);

        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);

        transferViewModel.recepientAddress.observe(getActivity(), s -> recipientAddressTextView.setText(s));

        presenter = new ConfirmTransferPresenter();
        presenter.init(this);

        return view;
    }

    @OnClick(R.id.back_button) void backButtonPressed(){
        ((TransferFragment) getParentFragment()).goToTransferInfo();
    }

    @OnClick(R.id.confirm_transfer_button) void confirmButtonPressed(){

    }
}
