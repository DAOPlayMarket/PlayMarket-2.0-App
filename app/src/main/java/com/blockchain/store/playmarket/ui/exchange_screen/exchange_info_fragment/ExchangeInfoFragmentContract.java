package com.blockchain.store.playmarket.ui.exchange_screen.exchange_info_fragment;

import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ExchangeInfoFragmentContract {
    public interface View {
        void showLoadCurrenciesProgress(boolean isShown);

        void onEstimatedAmountReady(String result);

        void onEstimatedAmountFail(Throwable throwable);

        void onMinimumAmountReady(String minimumAmount);

        void onMinimumAmountError(Throwable throwable);
    }


    public interface Presenter {
        void init(View view);

        void getEstimatedAmount(String name, String amount);

        void loadMinimumAmount(ChangellyCurrency changellyCurrency);
    }
}
