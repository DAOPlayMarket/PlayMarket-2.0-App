package com.blockchain.store.playmarket.ui.exchange_screen;

import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ExchangeActivityContract {
    public interface View {
        void showLoadCurrenciesProgress(boolean isShown);

        void onLoadCurrenciesReady(ArrayList<ChangellyCurrency> currencies);

        void onLoadCurrenciesFailed(Throwable throwable);

        void onEstimatedAmountReady(String result);

        void onEstimatedAmountFail(Throwable throwable);

        void onTransactionCreatedSuccessfully(ChangellyCreateTransactionResponse changellyCreateTransactionResponse);

        void onTransactionCreatedFailed(Throwable throwable);
    }

    public interface Presenter {
        void init(View view);
        void loadAllCurrencies();

        void getEstimatedAmount(String name, String amount);

        void createTransaction(String from, String address, String amount, String extraId);
    }
}
