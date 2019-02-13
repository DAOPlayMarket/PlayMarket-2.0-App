package com.blockchain.store.playmarket.repositories;

import com.orhanobut.hawk.Hawk;

import java.util.Currency;
import java.util.Locale;

import static com.blockchain.store.playmarket.utilities.Constants.SETTINGS_USER_CURRENCY;

public class CurrencyRepository {

    public static String getUserCurrency() {
        String currencyCode;
        if (Hawk.contains(SETTINGS_USER_CURRENCY)) {
            currencyCode = Hawk.get(SETTINGS_USER_CURRENCY);
        } else {
            try {
                currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
            } catch (NullPointerException | IllegalArgumentException ex) {
                currencyCode = Currency.getInstance(new Locale("en", "US")).getCurrencyCode();
            }
        }
        Hawk.put(SETTINGS_USER_CURRENCY, currencyCode);
        return currencyCode;

    }
}
