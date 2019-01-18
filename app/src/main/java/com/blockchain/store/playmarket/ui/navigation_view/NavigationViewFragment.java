package com.blockchain.store.playmarket.ui.navigation_view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.ui.about_screen.AboutAppActivity;
import com.blockchain.store.playmarket.ui.my_apps_screen.MyAppsActivity;
import com.blockchain.store.playmarket.ui.settings_screen.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationViewFragment extends Fragment implements NavigationViewContract.View, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NavigationViewFragment";

    private NavigationCallback navigationCallback;

    @BindView(R.id.library_layout) LinearLayout libraryLayout;
    //@BindView(R.id.scroll_view) NestedScrollView scrollView;
    //@BindView(R.id.news_layout) LinearLayout newsLayout;
    @BindView(R.id.settings_layout) LinearLayout settingsLayout;
    //@BindView(R.id.my_ico_layout) LinearLayout myIcoLayout;
    @BindView(R.id.about_layout) LinearLayout aboutLayout;
    //@BindView(R.id.token_layout) LinearLayout tokenLayout;
    //@BindView(R.id.user_id_title) TextView userAddress;
    @BindView(R.id.ether_count) TextView balanceView;
    // @BindView(R.id.avatar_image) ImageView avatarImage;
    //@BindView(R.id.user_balance_progress_bar) ProgressBar progressBar;
    //@BindView(R.id.error_view_title) TextView errorViewTitle;
    //@BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.error_view_repeat_btn) Button errorBtn;
    // @BindView(R.id.balance_icon) ImageView balanceIcon;
    @BindView(R.id.balance_in_local) TextView balanceInLocal;

    NavigationViewPresenter presenter;

    public NavigationViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigationCallback = (NavigationCallback) context;
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
        //userAddress.setText(AccountManager.getFormattedAddress());
        //avatarImage.setImageBitmap(QrUtils.getBitmapQrFromAddress(AccountManager.getAddress().getHex(), QrUtils.QR_SIZE.SMALL));
        //swipeRefreshLayout.setOnRefreshListener(this);
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
        startActivity(new Intent(getActivity(), MyAppsActivity.class));
    }

    @OnClick(R.id.votes_layout)
    void onVotesClicked() {
        navigationCallback.onVotesClicked();
    }

    @OnClick(R.id.dividends_layout)
    void onDividendsClicked() {
        navigationCallback.onDividendsClicked();
    }

//    @OnClick(R.id.news_layout)
//    void onNewItemClicked() {
//        closeDrawers();
//        startActivity(new Intent(getActivity(), NewsActivity.class));
//    }

//    @OnClick(R.id.nav_view_send)
//    void showAddFundsDialog() {
//        startActivity(new Intent(getActivity(), TransferActivity.class));
//    }

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

//    @OnClick(R.id.account_layout)
//    void onAccountClicked() {
//        closeDrawers();
//        Intent intent = new Intent(getActivity(), NewUserWelcomeActivity.class);
//        intent.putExtra(Constants.WELCOME_ACTIVITY_IS_LUANCHED_FROM_SETTINGS_EXTRA, true);
//        startActivity(intent);
//    }

//    @OnClick(R.id.user_id_title)
//    void copyAddressToClipBoard() {
//        ClipboardUtils.copyToClipboard(getActivity(), userAddress.getText().toString().replaceAll(" ", ""));
//        ToastUtil.showToast(R.string.address_copied);
//    }

//    @OnClick(R.id.my_ico_layout)
//    void onMyIcoClicked() {
//        closeDrawers();
//        startActivity(new Intent(getActivity(), MyIcoActivity.class));
//    }

//    @OnClick(R.id.nav_view_history)
//    void onHistoryClicked() {
//        closeDrawers();
//        startActivity(new Intent(getActivity(), TransactionHistoryActivity.class));
//    }

//    @OnClick(R.id.token_layout)
//    void onTokenClicked() {
//        closeDrawers();
//        startActivity(new Intent(getActivity(), TokenListActivity.class));
//    }


    @Override
    public void onBalanceReady(UserBalance balance) {
        //swipeRefreshLayout.setRefreshing(false);
        //errorViewTitle.setVisibility(View.GONE);
        //errorBtn.setVisibility(View.GONE);

        //balanceView.setVisibility(View.VISIBLE);
        //balanceIcon.setVisibility(View.VISIBLE);
        //balanceInLocal.setVisibility(View.VISIBLE);

        //AccountManager.setUserBalance(balance.balanceInWei);
        //balanceView.setText(new EthereumPrice(balance.balanceInWei).inEther().toString());
        //balanceInLocal.setText(String.format(getString(R.string.local_currency), balance.symbol, balance.getFormattedLocalCurrency()));
    }

    @Override
    public void onBalanceFail(Throwable throwable) {
        //swipeRefreshLayout.setRefreshing(false);
        //errorViewTitle.setVisibility(View.VISIBLE);
        //errorBtn.setVisibility(View.VISIBLE);

        //balanceView.setVisibility(View.INVISIBLE);
        //balanceIcon.setVisibility(View.INVISIBLE);
        //balanceInLocal.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUserBalanceProgress(boolean isShow) {
        //progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
//
//    @OnClick(R.id.nav_view_exchange)
//    void onExchangeClicked() {
//        startActivity(new Intent(getActivity(), ExchangeActivity.class));
//    }
//
//    @OnClick(R.id.error_view_repeat_btn)
//    void onRepeatButtonClicked() {
//        errorViewTitle.setVisibility(View.GONE);
//        errorBtn.setVisibility(View.GONE);
//        presenter.loadUserBalance();
//    }


    @Override
    public void onRefresh() {
        presenter.loadUserBalance();
    }
}
