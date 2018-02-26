package com.blockchain.store.playmarket.ui.navigation_view;

/**
 * Created by Crypton04 on 09.02.2018.
 */

public class NavigationViewContract {
    interface View {

        void onBalanceReady(String balance);

        void onBalanceFail(Throwable throwable);

        void showUserBalanceProgress(boolean isShow);
    }

    interface Presenter {
        void init(View view);

        void loadUserBalance();
    }
}
