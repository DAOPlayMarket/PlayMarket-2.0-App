package com.blockchain.store.playmarket.ui.token_transfer_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;
import com.blockchain.store.playmarket.utilities.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenTransferFragment extends Fragment implements TokenTransferContract.View {

    private static String TOKEN_TAG = "token";
    private static String CRYPTODUEL_TAG = "cryptoduel_tag";
    private static final String TAG = "TokenTransferFragment";
    private int tabPosition = 0;

    @BindView(R.id.tokenTitle_textView) TextView tokenTitleTextView;
    @BindView(R.id.balance_textView) TextView balanceTextView;
    @BindView(R.id.repositoryBalance_textView) TextView repositoryBalanceTextView;
    @BindView(R.id.token_textView) TextView tokenTextView;
    @BindView(R.id.token2_textView) TextView token2TextView;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.send_group) Group sendGroup;
    @BindView(R.id.send_InputLayout) TextInputLayout sendInputLayout;
    @BindView(R.id.send_EditText) TextInputEditText sendEditText;
    @BindView(R.id.repository_textView) TextView repositoryTextView;
    @BindView(R.id.recipient_editText) TextInputEditText recipientEditText;
    @BindView(R.id.textView3) TextView balanceRepositoryTitle;
    @BindView(R.id.repository_button) RadioButton repositoryButton;
    @BindView(R.id.customAddress_button) RadioButton customAddressButton;
    @BindView(R.id.qrScanner_button) ImageView qrScannerButton;
    @BindView(R.id.lockedAmount) TextView lockedAmount;
    @BindView(R.id.recipient_radioGroup) RadioGroup radioGroup;
    @BindView(R.id.all_button) TextView allTv;

    @BindView(R.id.continue_button) Button continueButton;
    private boolean isOpenAsCryptoDuelToken = false;
    private int currentTabPosition = 0;
    private DaoToken daoToken;
    private TokenTransferPresenter presenter;

    public static TokenTransferFragment newInstance(DaoToken daoToken) {
        Bundle args = new Bundle();
        args.putParcelable(TOKEN_TAG, daoToken);
        TokenTransferFragment fragment = new TokenTransferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TokenTransferFragment openAsCryptoDuelToken(DaoToken daoToken) {
        Bundle args = new Bundle();
        args.putParcelable(TOKEN_TAG, daoToken);
        args.putBoolean(CRYPTODUEL_TAG, true);
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
        attachPresenter();
        if (getArguments() != null) {
            this.daoToken = getArguments().getParcelable(TOKEN_TAG);
            this.isOpenAsCryptoDuelToken = getArguments().getBoolean(CRYPTODUEL_TAG, false);
            if (isOpenAsCryptoDuelToken) {
                initAsCryptoDuelProvided(daoToken);
            } else if (daoToken != null) {
                initAsDaoTokenProvided(daoToken);
                initStartView();
            }
        }

        initTabLayout();
        initRadioGroup();
    }

    private void attachPresenter() {
        presenter = new TokenTransferPresenter();
        presenter.init(this);
    }

    private void initAsDaoTokenProvided(DaoToken daoToken) {
        tokenTitleTextView.setText(daoToken.name);
        balanceTextView.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
        repositoryBalanceTextView.setText(String.valueOf(daoToken.getDaoBalanceWithDecimals()));
        tokenTextView.setText(daoToken.symbol);
        token2TextView.setText(daoToken.symbol);
        if (daoToken.isWithdrawBlocked) {
            lockedAmount.setText(R.string.all_token_are_locked);
        } else {
            lockedAmount.setText(String.format(getString(R.string.token_are_locked), daoToken.getDaoBalance() - daoToken.getNotLockedBalance()));
        }
    }

    private void initAsCryptoDuelProvided(DaoToken daoToken) {
        tokenTitleTextView.setText(daoToken.name);
        balanceTextView.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
        tokenTextView.setText(daoToken.symbol);
        token2TextView.setText(getString(R.string.eth));

        balanceRepositoryTitle.setText(getActivity().getString(R.string.dividends_balance));
        repositoryBalanceTextView.setText(daoToken.getOwnersBal() + getString(R.string.eth));

        repositoryButton.setVisibility(View.GONE);
        customAddressButton.setVisibility(View.GONE);
        lockedAmount.setVisibility(View.GONE);
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
            sendTab.setText(R.string.send);
        }

        TabLayout.Tab withdrawTab = tabLayout.getTabAt(1);
        if (withdrawTab != null) withdrawTab.setText(R.string.withdraw);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();

                switch (tab.getPosition()) {
                    case 0:
                        showSendComponents();
                        tabPosition = 0;
                        break;
                    case 1:
                        showWithdrawComponents();
                        tabPosition = 1;
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
        if (isOpenAsCryptoDuelToken) token2TextView.setText(daoToken.symbol);
    }

    private void showWithdrawComponents() {
        sendGroup.setVisibility(View.GONE);
        repositoryTextView.setVisibility(View.VISIBLE);
        qrScannerButton.setVisibility(View.GONE);
        if (repositoryTextView.getText() == "")
            repositoryTextView.setText(AccountManager.getAddress().getHex());
        sendInputLayout.setHint(getResources().getString(R.string.withdraw_amount));
        if (isOpenAsCryptoDuelToken) token2TextView.setText(R.string.eth);
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


    private boolean checkEnterValue() {
        Double sendAmount = 0d;
        try {
            sendAmount = Double.valueOf(sendEditText.getText().toString());
        } catch (Exception e) {
            sendEditText.setText("0");
        }

        if (!AccountManager.getUserBalance().equalsIgnoreCase("-1") && Long.valueOf(AccountManager.getUserBalance()) == 0) {
            sendEditText.setError(getString(R.string.not_enough_balance));
            return false;
        }

        if (sendAmount == 0) {
            sendEditText.setError(getString(R.string.wrong_invest_amout));
            return false;
        }

        if (isOpenAsCryptoDuelToken) {

            if (currentTabPosition == 0) {
                if (sendAmount > daoToken.getBalanceWithDecimals()) {
                    sendEditText.setError(String.format(getString(R.string.wrong_send_token_number), daoToken.getBalanceWithDecimals()));
                    return false;
                }
            }
            if (currentTabPosition == 1) {
                if (sendAmount > Double.valueOf(daoToken.getOwnersBal())) {
                    sendEditText.setError(String.format(getString(R.string.wrong_send_token_number), daoToken.getBalanceWithDecimals()));
                    return false;
                }
            }


            return true;
        }

        if (currentTabPosition == 0) {
            if (repositoryButton.isChecked()) {
                if (sendAmount > daoToken.getApprovalWithDecimals() && daoToken.getApprovalWithDecimals() != 0) {
                    String errorMsg = String.format(getString(R.string.you_already_has_approval_tokens), daoToken.getApprovalWithDecimals());
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (sendAmount > Double.valueOf(daoToken.getBalanceWithDecimals())) {
                    sendEditText.setError(String.format(getString(R.string.wrong_send_token_number), daoToken.getBalanceWithDecimals()));
                    return false;
                }
            }
            if (customAddressButton.isChecked()) {
                if (recipientEditText.getText().toString().isEmpty()) {
                    recipientEditText.setError(getString(R.string.empty_field));
                    return false;
                }
                if (sendAmount > daoToken.getBalanceWithDecimals()) {
                    sendEditText.setError(String.format(getString(R.string.wrong_send_token_number), daoToken.getBalanceWithDecimals()));
                    return false;
                }
            }
        }
        if (currentTabPosition == 1) {/*withdraw*/

            if (daoToken.isWithdrawBlocked) {
                ToastUtil.showToast(R.string.withdraw_is_blocked);
                return false;
            }

            if (sendAmount > Double.valueOf(daoToken.getNotLockedBalanceWithDecimals())) {
                String format = String.format(getString(R.string.can_withdraw), Long.valueOf(daoToken.getNotLockedBalanceWithDecimals()));
                sendEditText.setError(format);
                return false;
            }
        }

        return true;
    }

    private void sendTokenToUser(Long amount) {
        if (recipientEditText.getText().toString().isEmpty()) {
            recipientEditText.setError(getString(R.string.empty_field));
            return;
        }
        new DialogManager().showDividendsDialog(getActivity(), () ->
                presenter.sendTokenToUser(amount, recipientEditText.getText().toString(), isOpenAsCryptoDuelToken)
        );

    }

    private void sendTokensToRepository(Long amount) {
        new DialogManager().showDividendsDialog(getActivity(), new DialogManager.DividendCallback() {
            @Override
            public void onAccountUnlocked() {
                presenter.sendTokenToRepository(daoToken.getApprovalWithoutDecimal(), amount);
//                RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
//                        .flatMap(result -> {
//                            try {
//                                String rawTransaction;
//                                Long approvalWithoutDecimal = daoToken.getApprovalWithoutDecimal();
//
//                                if (approvalWithoutDecimal >= amount && approvalWithoutDecimal != 0) {
//                                    Transaction transaction = CryptoUtils.generateDepositOnlyTokenToRepositoryTx(result, amount);
//                                    TransactionInteractor.addToJobSchedule(transaction.getHash().getHex(), Constants.TransactionTypes.SEND_INTO_REPOSITORY);
//                                    rawTransaction = CryptoUtils.getRawTransaction(transaction);
//                                } else {
//                                    Pair<Transaction, Transaction> stringStringPair = CryptoUtils.generateDepositTokenToRepositoryTx(result, amount);
//                                    rawTransaction = CryptoUtils.getRawTransaction(stringStringPair.first);
//                                    String rawSecondTransaction = CryptoUtils.getRawTransaction(stringStringPair.second);
//                                    TransactionInteractor.addToJobSchedule(stringStringPair.first.getHash().getHex(), stringStringPair.second.getHash().getHex(), rawSecondTransaction, Constants.TransactionTypes.SEND_INTO_REPOSITORY);
//                                }
//                                return RestApi.getServerApi().deployTransaction(rawTransaction);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                throw new RuntimeException("111");
//                            }
//                        })
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(response -> transferSuccess(response), error -> transferFailed(error));
            }

        });

    }


    private void proceedWithWithdraw(Long amount) {/*WORKS need refactor*/
        presenter.withdraw(amount, isOpenAsCryptoDuelToken);
//        new DialogManager().showDividendsDialog(getActivity(), new DialogManager.DividendCallback() {
//            @Override
//            public void onAccountUnlocked() {
//                RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex())
//                        .flatMap(result -> {
//                            try {
//                                Transaction tx;
//                                if (isOpenAsCryptoDuelToken) {
//                                    tx = CryptoUtils.generateWithDrawCDLTTokens(result, amount);
//                                } else {
//                                    tx = CryptoUtils.generateWithDrawPmtTokens(result, amount);
//                                }
//
//                                String rawTx = CryptoUtils.getRawTransaction(tx);
//                                new TransactionInteractor().addToJobSchedule(tx.getHash().getHex(), Constants.TransactionTypes.WITHDRAW_TOKEN);
//                                return RestApi.getServerApi().deployTransaction(rawTx);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                throw new RuntimeException("111");
//                            }
//                        })
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(response -> transferSuccess(response), error -> transferFailed(error));
//            }
//
//        });

    }


    @OnClick({R.id.close_button, R.id.cancel_button})
    void onCloseButtonPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    //    @OnClick(R.id.continue_button)
    void onContinueButtonPressed() {
        String address;
        if (tabPosition == 0) address = recipientEditText.getText().toString();
        else address = repositoryTextView.getText().toString();
        String amount = sendEditText.getText().toString();

        new DialogManager().showPasswordDialogWithDetails(amount, address, getContext(), (isUnlock) -> {

        });
    }

    @OnClick(R.id.continue_button)
    void onContinueClicked() {
        if (!checkEnterValue()) {
            return;
        }

        if (isOpenAsCryptoDuelToken) {
            proceedAsCDLTtoken();
        } else {
            proceedAsPmtToken();
        }
    }

    private void proceedAsCDLTtoken() {
        if (currentTabPosition == 0) {
            long amount = 0L;
            try {
                Double value = Double.valueOf(sendEditText.getText().toString()) * Math.pow(10, daoToken.decimals);
                amount = value.longValue();
            } catch (Exception e) {
                sendEditText.setError(getString(R.string.wrong_invest_amout));
                return;
            }
            sendTokenToUser(amount);
        } else {
            Long amount = new EthereumPrice(sendEditText.getText().toString(), EthereumPrice.Currency.ETHER).wei.longValue();
            proceedWithWithdraw(amount);
        }
    }

    private void proceedAsPmtToken() {
        Long amount = 0L;
        try {
            Double value = Double.valueOf(sendEditText.getText().toString()) * Math.pow(10, daoToken.decimals);
            amount = value.longValue();
        } catch (Exception e) {
            sendEditText.setError(getString(R.string.wrong_invest_amout));
            return;
        }


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

    @OnClick(R.id.all_button)
    void onAllBtnClicked() {

        if (isOpenAsCryptoDuelToken) {

            if (currentTabPosition == 0) {
                sendEditText.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
            }

            if (currentTabPosition == 1) {
                sendEditText.setText(daoToken.getOwnersBal());
            }
            return;
        }

        if (currentTabPosition == 0) {
            if (repositoryButton.isChecked()) {
                if (daoToken.getApprovalWithDecimals() != 0) {
                    sendEditText.setText(String.valueOf(daoToken.getApprovalWithDecimals()));
                } else {
                    sendEditText.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
                }
            }
            if (customAddressButton.isChecked()) {
                sendEditText.setText(String.valueOf(daoToken.getBalanceWithDecimals()));
            }
        }
        if (currentTabPosition == 1) {/*withdraw*/
            sendEditText.setText(String.valueOf(daoToken.getNotLockedBalanceWithDecimals()));
        }
    }

    @Override
    public void transferSuccess(String response) {
        try {
            ToastUtil.showToast(R.string.transaction_sent);
            getActivity().onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transferFailed(Throwable error) {
        ToastUtil.showToast(error.getMessage());
    }
}
