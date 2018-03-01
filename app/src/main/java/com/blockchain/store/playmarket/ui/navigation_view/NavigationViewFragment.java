package com.blockchain.store.playmarket.ui.navigation_view;


import android.accounts.Account;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.AccountInfoResponse;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.about_screen.AboutAppActivity;
import com.blockchain.store.playmarket.ui.exchange_screen.ExchangeActivity;
import com.blockchain.store.playmarket.ui.library_screen.LibraryActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.ui.settings_screen.SettingsActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Blockies;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.FileUtils;

import org.ethereum.geth.Address;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NavigationViewFragment extends Fragment implements NavigationViewContract.View, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NavigationViewFragment";

    @BindView(R.id.wishlist_layout) LinearLayout wishlistLayout;
    @BindView(R.id.library_layout) LinearLayout libraryLayout;
    @BindView(R.id.news_layout) LinearLayout newsLayout;
    @BindView(R.id.history_layout) LinearLayout historyLayout;
    @BindView(R.id.settings_layout) LinearLayout settingsLayout;
    @BindView(R.id.ether_exchange_layout) LinearLayout etherExchangeLayout;
    @BindView(R.id.my_ico_layout) LinearLayout myIcoLayout;
    @BindView(R.id.about_layout) LinearLayout aboutLayout;
    @BindView(R.id.user_id_title) TextView userAddress;
    @BindView(R.id.ether_count) TextView balanceView;
    @BindView(R.id.avatar_image) ImageView avatarImage;
    @BindView(R.id.user_balance_holder) LinearLayout userBalanceHolder;
    @BindView(R.id.balance_error_holder) LinearLayout userBalanceErrorHolder;
    @BindView(R.id.user_balance_progress_bar) ProgressBar progressBar;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    NavigationViewPresenter presenter;

    public NavigationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        attachPresenter();
        setViews();
        return view;
    }

    private void setViews() {
        userAddress.setText(AccountManager.getAddress().getHex());
        avatarImage.setImageBitmap(Blockies.createIcon(AccountManager.getAddress().getHex().toLowerCase()));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void attachPresenter() {
        presenter = new NavigationViewPresenter();
        presenter.init(this);
        presenter.loadUserBalance();
    }

    @OnClick(R.id.close_image_button)
    void onCloseImageClicked() {
        closeDrawers();

    }

    @OnClick(R.id.library_layout)
    void onLibraryItemClicked() {
        closeDrawers();
        startActivity(new Intent(getActivity(), LibraryActivity.class));
    }

    @OnClick(R.id.nav_view_send)
    void showAddFundsDialog() {
        AlertDialog confirmImportDialog = new AlertDialog.Builder(getContext())
                .setView(R.layout.transfer_dialog)
                .setCancelable(false)
                .create();
        confirmImportDialog.show();

        AccountInfoResponse accountInfoResponse = new AccountInfoResponse();

        Observable<AccountInfoResponse> accountInfoResponseObservable = RestApi.getServerApi().getAccountInfo(AccountManager.getAddress().getHex());
        accountInfoResponseObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    accountInfoResponse.balance = data.balance;
                    accountInfoResponse.count = data.count;
                    accountInfoResponse.gasPrice = data.gasPrice;
                });

        EditText senderAddressEditText = (EditText) confirmImportDialog.findViewById(R.id.sender_address_editText);
        EditText senderPasswordEditText = (EditText) confirmImportDialog.findViewById(R.id.sender_password_editText);
        EditText payeeAddressEditText = (EditText) confirmImportDialog.findViewById(R.id.payee_address_editText);
        EditText transferAmountEditText = (EditText) confirmImportDialog.findViewById(R.id.transfer_amount_editText);
        Button confirmTransferButton = (Button) confirmImportDialog.findViewById(R.id.confirm_transfer_button);
        Button cancelTransferButton = (Button) confirmImportDialog.findViewById(R.id.cancel_transfer_button);
    }

    @OnClick(R.id.settings_layout)
    void onSettingsClicked() {
        closeDrawers();
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    public void closeDrawers() {
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.about_layout)
    void onAboutClicked() {
        closeDrawers();
        startActivity(new Intent(getActivity(), AboutAppActivity.class));
    }

    @OnClick(R.id.account_layout)
    void onAccountClicked() {
        closeDrawers();
        Intent intent = new Intent(getActivity(), NewUserWelcomeActivity.class);
        intent.putExtra(Constants.WELCOME_ACTIVITY_IS_LUANCHED_FROM_SETTINGS_EXTRA, true);
        startActivity(intent);
    }

    @Override
    public void onBalanceReady(String balance) {
        swipeRefreshLayout.setRefreshing(false);
        userBalanceErrorHolder.setVisibility(View.GONE);
        userBalanceHolder.setVisibility(View.VISIBLE);
        AccountManager.setUserBalance(balance);
        balanceView.setText(new EthereumPrice(balance).inEther().toString());
    }

    @Override
    public void onBalanceFail(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        userBalanceErrorHolder.setVisibility(View.VISIBLE);
        userBalanceHolder.setVisibility(View.GONE);
    }

    @Override
    public void showUserBalanceProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.nav_view_exchange)
    void onExchangeClicked() {
        startActivity(new Intent(getActivity(), ExchangeActivity.class));
    }

    @OnClick(R.id.error_view_repeat_btn)
    void onRepeatButtonClicked() {
        presenter.loadUserBalance();
    }


    @Override
    public void onRefresh() {
        presenter.loadUserBalance();
    }
}
