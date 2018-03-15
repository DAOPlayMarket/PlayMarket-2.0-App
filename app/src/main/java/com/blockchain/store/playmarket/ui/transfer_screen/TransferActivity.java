package com.blockchain.store.playmarket.ui.transfer_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.TransferViewPagerAdapter;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferActivity extends AppCompatActivity {

    @BindView(R.id.transfer_viewPager)
    NonSwipeableViewPager transferViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        TransferViewPagerAdapter transferAdapter = new TransferViewPagerAdapter(getSupportFragmentManager());
        transferViewPager.setAdapter(transferAdapter);
    }

    public void goToConfirmTransfer() {
        transferViewPager.setCurrentItem(1, true);
    }

    public void goToTransferInfo() {
        transferViewPager.setCurrentItem(0, true);
    }
}
