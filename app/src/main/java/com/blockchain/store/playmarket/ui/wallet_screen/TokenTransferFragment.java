package com.blockchain.store.playmarket.ui.wallet_screen;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.DialogManager;
import com.blockchain.store.playmarket.utilities.QRCodeScannerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TokenTransferFragment extends Fragment {

    private static String TOKEN_TAG = "token";
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
    @BindView(R.id.repository_button) RadioButton repositoryButton;
    @BindView(R.id.customAddress_button) RadioButton customAddressButton;
    @BindView(R.id.qrScanner_button) ImageView qrScannerButton;

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
            DaoToken daoToken = getArguments().getParcelable(TOKEN_TAG);
            if (daoToken != null) {
                tokenTitleTextView.setText(daoToken.name);
                balanceTextView.setText(daoToken.balance);
                repositoryBalanceTextView.setText(daoToken.daoBalance);
                tokenTextView.setText(daoToken.symbol);
                token2TextView.setText(daoToken.symbol);
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


    @OnClick({R.id.close_button, R.id.cancel_button})
    void onCloseButtonPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.continue_button)
    void onContinueButtonPressed() {
        String address;
        if (tabPosition == 0) address = recipientEditText.getText().toString();
        else address = repositoryTextView.getText().toString();
        String amount = sendEditText.getText().toString();

        new DialogManager().showPasswordDialogWithDetails(amount, address, getContext(), (isUnlock) -> {

        });
    }
}
