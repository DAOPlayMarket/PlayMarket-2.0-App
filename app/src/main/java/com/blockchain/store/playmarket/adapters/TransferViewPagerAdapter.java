package com.blockchain.store.playmarket.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blockchain.store.playmarket.ui.transfer_screen.confirm_transfer_screen.ConfirmTransferFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen.TransferInfoFragment;


public class TransferViewPagerAdapter extends FragmentPagerAdapter {

    public TransferViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TransferInfoFragment();
            case 1:
                return new ConfirmTransferFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
