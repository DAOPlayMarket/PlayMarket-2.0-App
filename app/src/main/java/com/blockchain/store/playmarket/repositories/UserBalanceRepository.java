package com.blockchain.store.playmarket.repositories;

import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.CryptoPriceResponse;
import com.blockchain.store.playmarket.data.entities.ExchangeRate;
import com.blockchain.store.playmarket.data.entities.UserBalance;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserBalanceRepository {

    public Observable<UserBalance> getUserBalance(String accountAddress) {
        return RestApi.getServerApi().getBalance(accountAddress)
                .flatMap(this::mapWith, Pair::new)
                .map(this::onNext)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private UserBalance onNext(Pair<String, CryptoPriceResponse> resultPair) {
        ExchangeRate exchangeRate = Hawk.get(Constants.CURRENT_CURRENCY, new ExchangeRate());
        double currentPriceOfOnePMC = resultPair.second.getPrice();
        double convertedUserBalanceToPMC = Double.parseDouble(resultPair.first) / currentPriceOfOnePMC;
        double localCurrencyDecimals = Math.pow(10, exchangeRate.currency.getDecimals());

        UserBalance userBalance = new UserBalance();
        userBalance.balanceInWei = resultPair.first;
        userBalance.balanceInPMC = String.valueOf(convertedUserBalanceToPMC);
        userBalance.balanceInLocalCurrency = String.valueOf(convertedUserBalanceToPMC * exchangeRate.getRate() / localCurrencyDecimals);
        userBalance.symbol = exchangeRate.currency.name;

        return userBalance;
    }

    private Observable<CryptoPriceResponse> mapWith(String s) {
        return RestApi.getServerApi().getCryptoPrice("100"); // PMC always has decimals = 2
    }

    public static ExchangeRate getUserCurrency() {
        return Hawk.get(Constants.CURRENT_CURRENCY, new ExchangeRate());
    }

    public static void putUserCurrency(ExchangeRate exchangeRate) {
        Hawk.put(Constants.CURRENT_CURRENCY, exchangeRate);

    }


}
