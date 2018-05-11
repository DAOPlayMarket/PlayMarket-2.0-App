package com.blockchain.store.playmarket.ui.transfer_screen;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_confirm_screen.TransferConfirmFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen.TransferInfoFragment;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferActivity extends AppCompatActivity implements TransferContract.View, LifecycleOwner {

    private TransferViewModel transferViewModel;

    private TransferPresenter presenter;

    private String password;
    private String recipientAddress;
    private String transferAmount;
    private boolean isEth;

    @BindView(R.id.transfer_viewPager) NonSwipeableViewPager transferViewPager;
    ViewPagerAdapter transferAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);

        getDataFromViewModel();

        presenter = new TransferPresenter();
        presenter.init(this, getApplicationContext());

        transferAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        transferAdapter.addFragment(new TransferInfoFragment());
        transferAdapter.addFragment(new TransferConfirmFragment());
        transferViewPager.setAdapter(transferAdapter);
    }

    @OnClick(R.id.continue_transfer_button)
    void continueButtonClicked() {
        TransferInfoFragment transferInfoFragment = (TransferInfoFragment) transferAdapter.getItem(0);
        TransferConfirmFragment transferConfirmFragment = (TransferConfirmFragment) transferAdapter.getItem(1);
        if (transferViewPager.getCurrentItem() == 0
                && transferInfoFragment.isHasNoError()) {
            goToConfirmTransfer();
        }
        else if (transferViewPager.getCurrentItem() == 1){
            getDataFromViewModel();
            if (presenter.passwordCheck(password)) {
                if (isEth)
                    transferAmount = new EthereumPrice(transferAmount, EthereumPrice.Currency.ETHER).inLongToString();
                else
                    transferAmount = new EthereumPrice(transferAmount, EthereumPrice.Currency.WEI).inLongToString();
                presenter.createTransaction(transferAmount, recipientAddress);
            }
            else {
                transferConfirmFragment.showError();
            }
        }
    }

    @OnClick(R.id.cancel_transfer_button)
    void cancelButtonClicked() {
        back();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void goToConfirmTransfer() {
        transferViewPager.setCurrentItem(1, true);
    }

    private void goToTransferInfo() {
        transferViewPager.setCurrentItem(0, true);
    }

    private void back() {
        if (transferViewPager.getCurrentItem() == 1) {
            goToTransferInfo();
        } else {
            this.finish();
        }
    }

    @Override
    public void closeTransferActivity() {
        finish();
    }

    @Override
    public void showToast(String message) {

    }

    private void getDataFromViewModel() {
        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);

        transferViewModel.recipientAddress.observe(this, s -> recipientAddress = s);
        transferViewModel.transferAmount.observe(this, s -> transferAmount = s);
        transferViewModel.senderPassword.observe(this, s -> password = s);
        transferViewModel.isEth.observe(this, aBoolean -> isEth = aBoolean);
    }
}
