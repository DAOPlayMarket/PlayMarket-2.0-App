package com.blockchain.store.playmarket.ui.transfer_screen;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppInfo;
import com.blockchain.store.playmarket.data.entities.ExchangeRate;
import com.blockchain.store.playmarket.data.entities.InvestTransactionModel;
import com.blockchain.store.playmarket.data.entities.SendTokenTransactionModel;
import com.blockchain.store.playmarket.data.entities.Token;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.repositories.UserBalanceRepository;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_confirm_screen.TransferConfirmFragment;
import com.blockchain.store.playmarket.ui.transfer_screen.transfer_info_screen.TransferInfoFragment;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FingerprintUtils;
import com.blockchain.store.playmarket.utilities.NonSwipeableViewPager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.ViewPagerAdapter;

import java.util.Currency;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferActivity extends AppCompatActivity implements TransferContract.View, LifecycleOwner {

    public static String RECIPIENT_ARG = "recipient_address";
    public static String APP_ARG = "app_address";
    public static String APP_INFO_ARG = "app_info_address";
    public static String TOKEN_INFO_ARG = "token_info_address";
    public static String TRANSACTION_ARG = "transaction_arg";
    public static String MAX_VALUE_ARG = "wei_max_value";
    public static String MIN_VALUE_ARG = "wei_min_value";

    @BindView(R.id.transfer_viewPager) NonSwipeableViewPager transferViewPager;
    @BindView(R.id.continue_transfer_button) Button continueButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private TransferViewModel transferViewModel;

    private TransferPresenter presenter;

    private String password;
    private String recipientAddress;
    private String transferAmount;
    private boolean isEth;

    private boolean isBlockEth;
    private App app;
    private AppInfo appInfo;
    private Token token;

    private ViewPagerAdapter transferAdapter;
    private String tokenName;
    private Constants.TransactionTypes transactionTypes;

    private long minSendValue;
    private long maxSendValue;

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

    public static void startAsTokenTransfer(Activity activity, Token token) {
        Intent starter = new Intent(activity.getBaseContext(), TransferActivity.class);
        starter.putExtra(TOKEN_INFO_ARG, token);
        starter.putExtra(TRANSACTION_ARG, Constants.TransactionTypes.TRANSFER_TOKEN);
        activity.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);

        getDataFromViewModel();
        transactionTypes = getIntent().getParcelableExtra(TRANSACTION_ARG);
        recipientAddress = getIntent().getStringExtra(RECIPIENT_ARG);
        if (recipientAddress != null) {
            transferViewModel.recipientAddress.setValue(recipientAddress);
        }

        checkMinMaxValues();
        app = getIntent().getParcelableExtra(APP_ARG);
        appInfo = getIntent().getParcelableExtra(APP_INFO_ARG);
        token = getIntent().getParcelableExtra(TOKEN_INFO_ARG);

        if (app != null) {
            ExchangeRate userCurrency = UserBalanceRepository.getUserCurrency();
            transferViewModel.tokenName.setValue(userCurrency.currency.name);
            transferViewModel.isBlockEthIcon.setValue(true);
            transferViewModel.transferAmount.setValue(app.getPrice());
            transferViewModel.transactionType.setValue(Constants.TransactionTypes.BUY_APP);
        } else if (appInfo != null) {
            transferViewModel.isBlockEthIcon.setValue(false);
            transferViewModel.totalBalance.setValue(appInfo.icoBalance.getTokenCount());
            transferViewModel.tokenName.setValue(appInfo.icoSymbol);
        } else if (token != null) {
            transferViewModel.isBlockEthIcon.setValue(false);
            transferViewModel.totalBalance.setValue(token.getTokenCount());
            transferViewModel.tokenName.setValue(token.symbol);
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

    private void checkMinMaxValues() {
        maxSendValue = getIntent().getLongExtra(MAX_VALUE_ARG, 0);
        minSendValue = getIntent().getLongExtra(MIN_VALUE_ARG, 0);

        transferViewModel.maxValue.postValue(maxSendValue);
        transferViewModel.minValue.postValue(minSendValue);

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
            showProgress();
            getDataFromViewModel();
            if (presenter.passwordCheck(password)) {
                if (appInfo != null) {
                    double pow = Math.pow(10, Double.parseDouble(appInfo.icoBalance.decimals));
                    Long totalTokens = (long) (Double.parseDouble(transferAmount) * pow);
                    String transformedAmount = totalTokens.toString();
                    presenter.createTransferTokenTransaction(transformedAmount, recipientAddress, appInfo.adrICO,
                            createInvestTransactionModel(transferAmount, appInfo.icoBalance.getTokenCount()));
                    return;
                }
                if (token != null) {
                    double pow = Math.pow(10, Double.parseDouble(token.decimals));
                    Long totalTokens = (long) (Double.parseDouble(transferAmount) * pow);
                    String transformedAmount = totalTokens.toString();
                    presenter.createTransferTokenTransaction(transformedAmount, recipientAddress, token.contract,
                            createTokenTransactionModel(transferAmount, token.balanceOf));
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

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        continueButton.setEnabled(false);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        continueButton.setEnabled(true);
    }

    private SendTokenTransactionModel createTokenTransactionModel(String transformedAmount, String wasTokenBefore) {
        SendTokenTransactionModel transactionModel = new SendTokenTransactionModel();
        transactionModel.tokenCount = transformedAmount;
        transactionModel.tokenCurrency = transferViewModel.tokenName.getValue();
        transactionModel.wasTokenBeforeTransaction = wasTokenBefore;
        transactionModel.appInfo = this.appInfo;
        return transactionModel;
    }

    private InvestTransactionModel createInvestTransactionModel(String transformedAmount, String wasTokenBefore) {
        InvestTransactionModel transactionModel = new InvestTransactionModel();
        transactionModel.tokenCount = transformedAmount;
        transactionModel.tokenCurrency = transferViewModel.tokenName.getValue();
        transactionModel.wasTokenBeforeTransaction = wasTokenBefore;
        transactionModel.appInfo = this.appInfo;
        return transactionModel;
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
        hideProgress();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showToast(String message) {
        hideProgress();
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
