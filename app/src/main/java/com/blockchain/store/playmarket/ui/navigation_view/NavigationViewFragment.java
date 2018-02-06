package com.blockchain.store.playmarket.ui.navigation_view;


import android.app.Dialog;
import android.os.Bundle;
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
import com.blockchain.store.playmarket.utilities.ToastUtil;
import com.blockchain.store.playmarket.utilities.data.ClipboardUtils;
import com.blockchain.store.playmarket.utilities.net.APIUtils;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blockchain.store.playmarket.utilities.crypto.CryptoUtils.keyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationViewFragment extends Fragment {

    // Объявление кнопок на панели навигации.
    // Закрыь панель навигации.
    @BindView(R.id.close_image_button)
    ImageButton closeImageButton;

    // Пополнить счёт.
    @BindView(R.id.account_top_up_button)
    Button accountTopUpButton;


    // Объявление слоёв на панели навигации.
    // Список желаний.
    @BindView(R.id.wishlist_layout)
    LinearLayout wishlistLayout;

    // Библиотека.
    @BindView(R.id.library_layout)
    LinearLayout libraryLayout;

    // Библиотека.
    @BindView(R.id.news_layout)
    LinearLayout newsLayout;

    // История покупок.
    @BindView(R.id.history_layout)
    LinearLayout historyLayout;

    // Настройки.
    @BindView(R.id.settings_layout)
    LinearLayout settingsLayout;

    // Библиотека.
    @BindView(R.id.ether_exchange_layout)
    LinearLayout etherExchangeLayout;

    // Мои ICO.
    @BindView(R.id.my_ico_layout)
    LinearLayout myIcoLayout;

    // О приложении.
    @BindView(R.id.about_layout)
    LinearLayout aboutLayout;


    public NavigationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    // Метод закрытия панели навигации.
    @OnClick(R.id.close_image_button) void onCloseImageClicked(){
        DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    // Метод открытия диалога для пополнения счёта.
    @OnClick(R.id.account_top_up_button) void showAddFundsDialog(){
        final Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.add_funds_dialog);
        d.show();
    }
}
