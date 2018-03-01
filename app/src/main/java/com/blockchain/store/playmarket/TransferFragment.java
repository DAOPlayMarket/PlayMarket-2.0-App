package com.blockchain.store.playmarket;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blockchain.store.playmarket.adapters.TransferViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferDialogFragment extends DialogFragment {

    @BindView(R.id.transfer_viewPager) ViewPager transferViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_dialog, container, false);
        ButterKnife.bind(this, view);
        TransferViewPagerAdapter transferAdapter = new TransferViewPagerAdapter(getActivity().getSupportFragmentManager());
        transferViewPager.setAdapter(transferAdapter);

        return view;
    }
}
