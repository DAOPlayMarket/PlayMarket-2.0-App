package com.blockchain.store.playmarket.ui.transfer_screen;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TransferViewPagerAdapter;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferFragment extends DialogFragment {

    @BindView(R.id.transfer_viewPager)
    NonSwipeableViewPager transferViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_dialog, container, false);
        ButterKnife.bind(this, view);
        TransferViewPagerAdapter transferAdapter = new TransferViewPagerAdapter(getChildFragmentManager());
        transferViewPager.setAdapter(transferAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @OnClick(R.id.cancel_transfer_button)
    void cancelButtonPressed() {
        this.dismiss();
    }

    public void goToConfirmTransfer() {
        transferViewPager.setCurrentItem(1, true);
    }

    public void goToTransferInfo() {
        transferViewPager.setCurrentItem(0, true);
    }
}
