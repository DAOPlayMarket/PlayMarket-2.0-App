package com.blockchain.store.playmarket.ui.exchange_screen;

import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 14.03.2018.
 */

public class ExchangeActivityContracts {
    public interface View {

        void showLoadCurrenciesProgress(boolean isShown);

        void onLoadCurrenciesReady(ArrayList<ChangellyCurrency> changellyCurrencies, ChangellyMinimumAmountResponse second);

        void onLoadCurrenciesFailed(Throwable throwable);

        void onTransactionCreatedSuccessfully(ChangellyCreateTransactionResponse changellyCreateTransactionResponse);

        void onTransactionCreatedFailed(Throwable throwable);
    }

    public interface Presenter {
        void init(View view);

        void loadCurrencies();

        void createTransaction(String from, String address, String amount);
    }
}
