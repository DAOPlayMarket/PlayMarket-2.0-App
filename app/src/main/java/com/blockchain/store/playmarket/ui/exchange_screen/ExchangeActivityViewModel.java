package com.blockchain.store.playmarket.ui.exchange_screen;

import android.arch.lifecycle.ViewModel;

import com.blockchain.store.playmarket.data.entities.ChangellyCurrenciesResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ExchangeActivityViewModel extends ViewModel {
    public ChangellyCurrency chosenCurrency;
    public ArrayList<ChangellyCurrency> changellyCurrencies;
    public String userEnteredAmount;
    public String payinAddress;
}
