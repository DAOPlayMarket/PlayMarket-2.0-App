package com.blockchain.store.playmarket.ui.wallet_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends Fragment {

    private NavigationCallback navigationCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.close_button)
    void onCloseButtonClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.button)
    void onItemClick() { navigationCallback.onTokenTransferClicked(); }

}
