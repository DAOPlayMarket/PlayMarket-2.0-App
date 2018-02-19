package com.blockchain.store.playmarket.ui.navigation_view;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.types.EthereumPrice;
import com.blockchain.store.playmarket.ui.about_screen.AboutAppActivity;
import com.blockchain.store.playmarket.ui.library_screen.LibraryActivity;
import com.blockchain.store.playmarket.ui.new_user_welcome_activity.NewUserWelcomeActivity;
import com.blockchain.store.playmarket.ui.settings_screen.SettingsActivity;
import com.blockchain.store.playmarket.utilities.AccountManager;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;
import com.blockchain.store.playmarket.utilities.net.APIUtils;

import javax.net.ssl.KeyManager;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blockchain.store.playmarket.utilities.crypto.CryptoUtils.keyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationViewFragment extends Fragment implements NavigationViewContract.View {
    private static final String TAG = "NavigationViewFragment";

    @BindView(R.id.account_top_up_button) Button accountTopUpButton;
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

    NavigationViewPresenter presenter;

    public NavigationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, view);
        attachPresenter();
        setViews();
        return view;
    }

    private void setViews() {
        userAddress.setText(AccountManager.getAddress().getHex());
    }

    private void attachPresenter() {
        presenter = new NavigationViewPresenter();
        presenter.init(this);
        presenter.loadUserBalance();
    }


    // Метод закрытия панели навигации.
    @OnClick(R.id.close_image_button)
    void onCloseImageClicked() {
        closeDrawers();

    }

    @OnClick(R.id.library_layout)
    void onLibraryItemClicked() {
        closeDrawers();
        startActivity(new Intent(getActivity(), LibraryActivity.class));
    }

    // Метод открытия диалога для пополнения счёта.
    @OnClick(R.id.account_top_up_button)
    void showAddFundsDialog() {
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.add_funds_dialog);
        TextView textView = d.findViewById(R.id.balanceText);

        textView.setText(new EthereumPrice(AccountManager.getUserBalance()).inEther().toString());
        d.show();
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
        AccountManager.setUserBalance(balance);
        balanceView.setText(new EthereumPrice(balance).inEther().toString());
    }

    @Override
    public void onBalanceFail(Throwable throwable) {

    }


}
