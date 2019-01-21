package com.blockchain.store.playmarket.ui.wallet_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PurchaseAppResponse;
import com.blockchain.store.playmarket.repositories.TransactionInteractor;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;
import com.blockchain.store.playmarket.utilities.crypto.CryptoUtils;

import org.ethereum.geth.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenTransferFragment extends Fragment {

    private static String TOKEN_TAG = "token";
    private static final String TAG = "TokenTransferFragment";

    @BindView(R.id.tokenTitle_textView) TextView tokenTitleTextView;
    @BindView(R.id.balance_textView) TextView balanceTextView;
    @BindView(R.id.repositoryBalance_textView) TextView repositoryBalanceTextView;
    @BindView(R.id.token_textView) TextView tokenTextView;
    @BindView(R.id.token2_textView) TextView token2TextView;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.send_group) Group sendGroup;
    @BindView(R.id.send_InputLayout) TextInputLayout sendInputLayout;
    @BindView(R.id.repository_textView) TextView repositoryTextView;
    @BindView(R.id.repository_button) RadioButton repositoryButton;
    @BindView(R.id.customAddress_button) RadioButton customAddressButton;
    @BindView(R.id.qrScanner_button) ImageView qrScannerButton;
    @BindView(R.id.recipient_editText) TextView recipientEditText;
    @BindView(R.id.lockedAmount) TextView lockedAmount;
    @BindView(R.id.send_EditText) EditText sendEditText;

    @BindView(R.id.continue_button) Button continueButton;
    private int currentTabPosition = 0;
    private DaoToken daoToken;

    public static TokenTransferFragment newInstance(DaoToken daoToken) {
        Bundle args = new Bundle();
        args.putParcelable(TOKEN_TAG, daoToken);
        TokenTransferFragment fragment = new TokenTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_token_transfer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            this.daoToken = getArguments().getParcelable(TOKEN_TAG);
            if (daoToken != null) {
                tokenTitleTextView.setText(daoToken.name);
                balanceTextView.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
                repositoryBalanceTextView.setText(String.valueOf(daoToken.getDaoBalanceWithDecimals()));
                tokenTextView.setText(daoToken.symbol);
                token2TextView.setText(daoToken.symbol);
                lockedAmount.setText(daoToken.getDaoBalance() - daoToken.getNotLockedBalance() + " are locked.");
            }
        }
        initStartView();
        initTabLayout();
        initRadioGroup();
    }

    private void initStartView() {
        recipientEditText.setText(DaoConstants.Repository);
        recipientEditText.setEnabled(false);
        qrScannerButton.setVisibility(View.GONE);
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        TabLayout.Tab sendTab = tabLayout.getTabAt(0);
        if (sendTab != null) {
            sendTab.setText("SEND");
        }

        TabLayout.Tab withdrawTab = tabLayout.getTabAt(1);
        if (withdrawTab != null) withdrawTab.setText("WITHDRAW");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        showSendComponents();
                        break;
                    case 1:
                        showWithdrawComponents();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initRadioGroup() {
        repositoryButton.setOnClickListener(view -> {
            recipientEditText.setText(DaoConstants.Repository);
            qrScannerButton.setVisibility(View.GONE);
            recipientEditText.setEnabled(false);
        });

        customAddressButton.setOnClickListener(view -> {
            recipientEditText.setText("");
            qrScannerButton.setVisibility(View.VISIBLE);
            recipientEditText.setEnabled(true);
        });
    }

    private void showSendComponents() {
        sendGroup.setVisibility(View.VISIBLE);
        repositoryTextView.setVisibility(View.GONE);
        sendInputLayout.setHint(getResources().getString(R.string.amount));
        if (customAddressButton.isChecked()) qrScannerButton.setVisibility(View.VISIBLE);
    }

    private void showWithdrawComponents() {
        sendGroup.setVisibility(View.GONE);
        repositoryTextView.setVisibility(View.VISIBLE);
        qrScannerButton.setVisibility(View.GONE);
        if (repositoryTextView.getText() == "")
            repositoryTextView.setText(AccountManager.getAddress().getHex());
        sendInputLayout.setHint(getResources().getString(R.string.withdraw_amount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        recipientEditText.setText(data.getStringExtra("qrResult"));
    }


    @OnClick(R.id.qrScanner_button)
    void onScannerButtonPressed() {
        Intent intent = new Intent(getActivity(), QRCodeScannerActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.continue_button)
    void onContinueClicked() {
        if (!checkEnterValue()) {
            return;
        }
        Long amount = (long) (Long.valueOf(sendEditText.getText().toString()) * Math.pow(10, daoToken.decimals));
        if (currentTabPosition == 0) {
            if (repositoryButton.isChecked()) {
                sendTokensToRepository(amount);
            }
            if (customAddressButton.isChecked()) {
                sendTokenToUser(amount);
            }
        }
        if (currentTabPosition == 1) {/*withdraw*/
            proceedWithWithdraw(amount);
        }

    }

    private boolean checkEnterValue() {
        Double sendAmount = Double.valueOf(sendEditText.getText().toString());

        if (sendAmount == 0) {
            sendEditText.setError("Wrong amount");
            return false;
        }
//        if (sendAmount > Double.valueOf(daoToken.getNotLockedBalanceWithDecimals())) {
//            sendEditText.setError("You can send only " + daoToken.getNotLockedBalanceWithDecimals() + " tokens");
//            return false;
//        }


        return true;
    }

    private void sendTokenToUser(Long amount) {
        if (recipientEditText.getText().toString().isEmpty()) {
            recipientEditText.setError("Empty value");
            return;
        }
        new DialogManager().showDividendsDialog(getActivity(), () -> RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                .flatMap(result -> {
                    try {
                        Transaction signedTx = CryptoUtils.generateDaoSendTokenToUser(result, recipientEditText.getText().toString(), String.valueOf(amount));
                        String rawTx = CryptoUtils.getRawTransaction(signedTx);
                        TransactionInteractor.addToJobSchedule(signedTx.getHash().getHex());
                        return RestApi.getServerApi().deployTransaction(rawTx);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("111");
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> transferSuccess(response), error -> transferFailed(error)));

    }

    private void sendTokensToRepository(Long amount) {
        new DialogManager().showDividendsDialog(getActivity(), new DialogManager.DividendCallback() {
            @Override
            public void onAccountUnlocked() {
                RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                        .flatMap(result -> {
                            try {
                                Pair<Transaction, Transaction> stringStringPair = CryptoUtils.generateDepositTokenToRepositoryTx(result, amount);
                                String rawTransaction = CryptoUtils.getRawTransaction(stringStringPair.first);
                                String rawSecondTransaction = CryptoUtils.getRawTransaction(stringStringPair.second);
                                TransactionInteractor.addToJobSchedule(stringStringPair.first.getHash().getHex(), stringStringPair.second.getHash().getHex(), rawSecondTransaction);
                                return RestApi.getServerApi().deployTransaction(rawTransaction);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException("111");
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> transferSuccess(response), error -> transferFailed(error));
            }

        });

    }

    private void transferFailed(Throwable throwable) {
        Log.d(TAG, "transferFailed() called with: throwable = [" + throwable + "]");
    }

    private void transferSuccess(PurchaseAppResponse purchaseAppResponse) {
        Log.d(TAG, "transferSuccess() called with: purchaseAppResponse = [" + purchaseAppResponse + "]");
    }

    private void proceedWithWithdraw(Long amount) {/*WORKS need refactor*/
        new DialogManager().showDividendsDialog(getActivity(), new DialogManager.DividendCallback() {
            @Override
            public void onAccountUnlocked() {
                RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
                        .flatMap(result -> {
                            try {
                                Transaction tx = CryptoUtils.generateWithDrawPmtTokens(result, amount);
                                String rawTx = CryptoUtils.getRawTransaction(tx);
                                new TransactionInteractor().addToJobSchedule(tx.getHash().getHex());
                                return RestApi.getServerApi().deployTransaction(rawTx);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException("111");
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> transferSuccess(response), error -> transferFailed(error));
            }

        });

    }


    @OnClick({R.id.close_button, R.id.cancel_button})
    void onCloseButtonPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }
}
