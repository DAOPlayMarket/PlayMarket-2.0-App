package com.blockchain.store.playmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmTransferFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_confirm_transfer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.back_button) void backButtonPressed(){
        ((TransferFragment) getParentFragment()).goToTransferInfo();
    }
}
