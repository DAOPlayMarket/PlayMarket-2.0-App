package com.blockchain.store.playmarket.ui.navigation_view;

import com.blockchain.store.playmarket.data.entities.UserBalance;

/**
 * Created by Crypton04 on 09.02.2018.
 */

public class NavigationViewContract {
    public interface View {

        void onBalanceReady(UserBalance balance);

        void onBalanceFail(Throwable throwable);

        void showUserBalanceProgress(boolean isShow);
    }

    interface Presenter {
        void init(View view);

        void loadUserBalance();
    }
}
