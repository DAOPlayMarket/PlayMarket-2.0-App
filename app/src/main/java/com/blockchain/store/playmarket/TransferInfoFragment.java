package com.blockchain.store.playmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferInfoFragment extends Fragment {

    @BindView(R.id.continue_transfer_button)
    Button continueButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.continue_transfer_button) void continueButtonPressed(){
        ((TransferFragment) getParentFragment()).goToConfirmTransfer();
    }
}
