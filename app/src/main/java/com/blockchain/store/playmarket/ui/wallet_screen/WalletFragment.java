package com.blockchain.store.playmarket.ui.wallet_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.dao.ui.dao_activity.DaoActivity;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.DaoTokenAdapter;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.ui.exchange_screen.ExchangeActivity;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.ui.navigation_view.NavigationViewContract;
import com.blockchain.store.playmarket.ui.navigation_view.NavigationViewPresenter;
import com.blockchain.store.playmarket.ui.qr_screen.QrActivity;
import com.blockchain.store.playmarket.ui.transaction_history_screen.TransactionHistoryActivity;
import com.blockchain.store.playmarket.ui.transfer_screen.TransferActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends Fragment implements NavigationViewContract.View {


    private static final String TAG = "WalletFragment";

    @BindView(R.id.close_button) ImageView close_button;
    @BindView(R.id.textView1) TextView textView1;
    @BindView(R.id.textView2) TextView textView2;
    @BindView(R.id.address_textView) TextView userAddress;
    @BindView(R.id.qr_button) ImageView qr_button;
    @BindView(R.id.copy_button) ImageView copy_button;
    @BindView(R.id.addEth_button) ImageView addEth_button;
    @BindView(R.id.transfer_button) ImageView transfer_button;
    @BindView(R.id.history_button) ImageView history_button;
    @BindView(R.id.imageView1) ImageView imageView1;
    @BindView(R.id.ethBalance_textView) TextView ethBalance;
    @BindView(R.id.refreshBalance_button) ImageView refreshBalance;
    @BindView(R.id.rubBalance_textView) TextView balanceInLocal;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.view) View view;
    @BindView(R.id.button) Button button;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.token_progress_Bar) ProgressBar tokenProgressBar;
    @BindView(R.id.error_view_repeat_btn) Button error_view_repeat_btn;
    @BindView(R.id.error_holder) LinearLayout errorHolder;

    private NavigationCallback navigationCallback;
    private NavigationViewPresenter presenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        attachPresenter();
        userAddress.setText(AccountManager.getAddress().getHex());
    }

    private void attachPresenter() {
        presenter = new NavigationViewPresenter();
        presenter.init(this);
        loadUserBalance();
        loadPmtToken();
    }

    private void loadPmtToken() {
        presenter.loadPmtToken();
        tokenProgressBar.setVisibility(View.VISIBLE);
        errorHolder.setVisibility(View.GONE);
    }

    private void loadUserBalance() {
        presenter.loadUserBalance();
        progressBar.setVisibility(View.VISIBLE);
        refreshBalance.setVisibility(View.GONE);
    }

    @OnClick(R.id.close_button)
    void onCloseButtonClicked() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    @OnClick(R.id.button)
    void onItemClick() {
        navigationCallback.onTokenTransferClicked(new DaoToken());
    }

    @Override
    public void onBalanceReady(UserBalance balance) {
        AccountManager.setUserBalance(balance.balanceInWei);
        ethBalance.setText(new EthereumPrice(balance.balanceInWei).inEther().toString());
        balanceInLocal.setText(String.format(getString(R.string.local_currency), balance.symbol, balance.getFormattedLocalCurrency()));

        progressBar.setVisibility(View.GONE);
        refreshBalance.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBalanceFail(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        refreshBalance.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUserBalanceProgress(boolean isShow) {

    }

    @Override
    public void onLocalTokensReady(List<DaoToken> daoTokens) {
        tokenProgressBar.setVisibility(View.GONE);
        errorHolder.setVisibility(View.GONE);
        initAdapter(daoTokens);
    }

    @Override
    public void onLocalTokensError(Throwable throwable) {
        tokenProgressBar.setVisibility(View.GONE);
        errorHolder.setVisibility(View.VISIBLE);
    }

    private void initAdapter(List<DaoToken> daoTokens) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DaoTokenAdapter daoTokenAdapter = new DaoTokenAdapter(daoTokens, new DaoActivity.DaoAdapterCallback() {
            @Override
            public void onPmTokenClicked(DaoToken daoToken) {
                ((MainMenuActivity) getActivity()).onTokenTransferClicked(daoToken);
            }

            @Override
            public void onDaoTokenClicked(DaoToken daoToken) {
                TransferActivity.startAsTokenTransfer(getActivity(), daoToken);
            }
        }, true);
        recyclerView.setAdapter(daoTokenAdapter);
    }

    @OnClick(R.id.qr_button)
    void onQrButtonClicked() {
        startActivity(new Intent(getActivity(), QrActivity.class));
    }

    @OnClick(R.id.copy_button)
    void onCopyButtonClicked() {
        ClipboardUtils.copyToClipboard(getActivity(), userAddress.getText().toString().replaceAll(" ", ""));
        ToastUtil.showToast(R.string.address_copied);
    }

    @OnClick(R.id.addEth_button)
    void onAddEthClicked() {
        startActivity(new Intent(getActivity(), ExchangeActivity.class));
    }

    @OnClick(R.id.transfer_button)
    void onTransferClicked() {
        startActivity(new Intent(getActivity(), TransferActivity.class));
    }

    @OnClick(R.id.history_button)
    void onHistoryClicked() {
        startActivity(new Intent(getActivity(), TransactionHistoryActivity.class));
    }

    @OnClick(R.id.error_view_repeat_btn)
    void onErrorHolderClicked() {
        loadPmtToken();
    }

    @OnClick(R.id.refreshBalance_button)
    void onRefreshBalanceClicked() {
        loadUserBalance();
    }

    @OnClick(R.id.change_account)
    void onchangeAccountClicked() {
        navigationCallback.onChangeAccountClicked();
    }


    @OnClick(R.id.fab)
    void onFabClicked() {
        navigationCallback.onAddTokenClicked();
    }
}
