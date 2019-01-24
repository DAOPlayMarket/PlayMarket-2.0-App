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

import com.blockchain.store.dao.data.entities.DaoToken;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.interfaces.NavigationCallback;
import com.blockchain.store.playmarket.ui.about_screen.AboutAppActivity;
import com.blockchain.store.playmarket.ui.my_apps_screen.MyAppsActivity;
import com.blockchain.store.playmarket.ui.settings_screen.SettingsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationViewFragment extends Fragment implements NavigationViewContract.View, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NavigationViewFragment";

    private NavigationCallback navigationCallback;

    @BindView(R.id.library_layout) LinearLayout libraryLayout;
    @BindView(R.id.settings_layout) LinearLayout settingsLayout;
    @BindView(R.id.about_layout) LinearLayout aboutLayout;
    @BindView(R.id.ether_count) TextView balanceView;
    @BindView(R.id.error_view_repeat_btn) Button errorBtn;
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

    @OnClick(R.id.wallet_layout)
    void onWalletClicked() {
        navigationCallback.onWalletClicked();
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

    @Override
    public void onBalanceReady(UserBalance balance) {
    }

    @Override
    public void onBalanceFail(Throwable throwable) {
    }

    @Override
    public void showUserBalanceProgress(boolean isShow) {
        //progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLocalTokensReady(List<DaoToken> daoTokens) {

    }

    @Override
    public void onLocalTokensError(Throwable throwable) {

    }


    @Override
    public void onRefresh() {
        presenter.loadUserBalance();
    }
}
