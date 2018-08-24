package com.blockchain.store.playmarket.ui.transfer_screen;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_confirm_screen.TransferConfirmFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen.TransferInfoFragment;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferActivity extends AppCompatActivity implements TransferContract.View, LifecycleOwner {

    public static String RECIPIENT_ARG = "recipient_address";
    public static String APP_ARG = "app_address";
    public static String APP_INFO_ARG = "app_info_address";
    public static String TRANSACTION_ARG = "transaction_arg";

    public static String REVIEW_ARG = "review_arg";
    public static String VOTE_ARG = "vote_arg";
    public static String TX_INDEX_ARG = "tx_index_arg";
    public static String TOKEN_NAME_ARG = "token_name_arg";

    private TransferViewModel transferViewModel;

    private TransferPresenter presenter;

    private String password;
    private String recipientAddress;
    private String transferAmount;
    private boolean isEth;

    private boolean isBlockEth;
    private App app;
    private AppInfo appInfo;

    @BindView(R.id.transfer_viewPager) NonSwipeableViewPager transferViewPager;
    @BindView(R.id.continue_transfer_button) Button continueButton;
    ViewPagerAdapter transferAdapter;
    String tokenName;

    public static void startWithResult(Activity activity, String review, String vote, String txIndex, Constants.TransactionTypes transactionType, int resultCode) {
        Intent starter = new Intent(activity.getBaseContext(), TransferActivity.class);
        starter.putExtra(REVIEW_ARG, review);
        starter.putExtra(VOTE_ARG, vote);
        starter.putExtra(TX_INDEX_ARG, txIndex);
        starter.putExtra(TRANSACTION_ARG, transactionType);
        activity.startActivityForResult(starter, resultCode);
    }

    public static void startWithResult(Activity activity, String recipientAddress, App app, Constants.TransactionTypes transactionType, int resultCode) {
        Intent starter = new Intent(activity.getBaseContext(), TransferActivity.class);
        starter.putExtra(RECIPIENT_ARG, recipientAddress);
        starter.putExtra(APP_ARG, app);
        starter.putExtra(TRANSACTION_ARG, transactionType);
        activity.startActivityForResult(starter, resultCode);
    }

    public static void startAsTokenTransfer(Activity activity, AppInfo appInfo) {
        Intent starter = new Intent(activity.getBaseContext(), TransferActivity.class);
        starter.putExtra(APP_INFO_ARG, appInfo);
        starter.putExtra(TRANSACTION_ARG, Constants.TransactionTypes.TRANSFER_TOKEN);
        activity.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);

        getDataFromViewModel();

        recipientAddress = getIntent().getStringExtra(RECIPIENT_ARG);
        if (recipientAddress != null) {
            transferViewModel.recipientAddress.setValue(recipientAddress);
        }

        app = getIntent().getParcelableExtra(APP_ARG);
        appInfo = getIntent().getParcelableExtra(APP_INFO_ARG);

        if (app != null) {
            transferViewModel.isBlockEthIcon.setValue(true);
            String totalPrice = new EthereumPrice(app.price, EthereumPrice.Currency.WEI).inEther().toString();
            transferViewModel.transferAmount.setValue(totalPrice);
        } else if (appInfo != null) {
            transferViewModel.isBlockEthIcon.setValue(false);
            transferViewModel.totalBalance.setValue(Long.valueOf(appInfo.icoBalance.getTokenCount()));
            transferViewModel.tokenName.setValue(appInfo.icoSymbol);
        } else {
            transferViewModel.isBlockEthIcon.setValue(false);

        }

        presenter = new TransferPresenter();
        presenter.init(this, getApplicationContext());

        transferAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        transferAdapter.addFragment(new TransferInfoFragment());
        transferAdapter.addFragment(new TransferConfirmFragment());
        transferViewPager.setAdapter(transferAdapter);
    }

    @OnClick(R.id.continue_transfer_button)
    public void continueButtonClicked() {
        TransferInfoFragment transferInfoFragment = (TransferInfoFragment) transferAdapter.getItem(0);
        TransferConfirmFragment transferConfirmFragment = (TransferConfirmFragment) transferAdapter.getItem(1);

        if (transferViewPager.getCurrentItem() == 0
                && transferInfoFragment.isHasNoError()) {
            goToConfirmTransfer();
            if (checkFingerprint()) {
                transferConfirmFragment.initFingerprint();
                setContinueButtonVisibility(View.INVISIBLE);
            } else {
                transferConfirmFragment.initPassword();
                setContinueButtonVisibility(View.VISIBLE);
            }
        } else if (transferViewPager.getCurrentItem() == 1) {
            getDataFromViewModel();
            if (presenter.passwordCheck(password)) {
                if (appInfo != null) {
                    String transformedAmount = (Float.parseFloat(transferAmount) * (Integer.parseInt(appInfo.icoBalance.decimals));
                    presenter.createTransferTokenTransaction(transformedAmount, recipientAddress, appInfo.adrICO);
                    return;
                }
                if (isEth)
                    transferAmount = new EthereumPrice(transferAmount, EthereumPrice.Currency.ETHER).inLongToString();
                else
                    transferAmount = new EthereumPrice(transferAmount, EthereumPrice.Currency.WEI).inLongToString();
                if (app == null) {
                    presenter.createTransaction(transferAmount, recipientAddress);
                } else {
                    presenter.createBuyTransaction(app);
                }
            } else {
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
            TransferConfirmFragment transferConfirmFragment = (TransferConfirmFragment) transferAdapter.getItem(1);
            transferConfirmFragment.disposedFingerprint();
            setContinueButtonVisibility(View.VISIBLE);
            goToTransferInfo();
        } else {
            this.finish();
        }
    }

    @Override
    public void closeTransferActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(message);
    }

    private void getDataFromViewModel() {
        transferViewModel = ViewModelProviders.of(this).get(TransferViewModel.class);

        transferViewModel.recipientAddress.observe(this, s -> recipientAddress = s);
        transferViewModel.transferAmount.observe(this, s -> transferAmount = s);
        transferViewModel.senderPassword.observe(this, s -> password = s);
        transferViewModel.isEth.observe(this, aBoolean -> isEth = aBoolean);
        transferViewModel.isBlockEthIcon.observe(this, aBoolean -> isBlockEth = aBoolean);
    }

    private boolean checkFingerprint() {
        return FingerprintUtils.isFingerprintAvailibility(this);
    }

    public void setContinueButtonVisibility(int type) {
        continueButton.setVisibility(type);
    }
}
