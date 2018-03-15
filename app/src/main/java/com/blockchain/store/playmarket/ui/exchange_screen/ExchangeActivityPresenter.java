package com.blockchain.store.playmarket.ui.exchange_screen;

import android.util.Pair;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 14.03.2018.
 */

public class ExchangeActivityPresenter implements ExchangeActivityContracts.Presenter {
    private ExchangeActivityContracts.View view;

    @Override
    public void init(ExchangeActivityContracts.View view) {
        this.view = view;
    }

    @Override
    public void loadCurrencies() {
        RestApi.getChangellyApi().getCurrenciesFull(ChangellyBaseBody.getCurrenciesFull())
                .map(changellyCurrenciesResponse -> {
                    ArrayList<ChangellyCurrency> changellyCurrencies = new ArrayList<>();
                    for (ChangellyCurrency currency : changellyCurrenciesResponse.result) {
                        if (currency.enabled && !currency.name.equalsIgnoreCase("eth")) {
                            changellyCurrencies.add(currency);
                        }
                    }
                    return changellyCurrencies;
                })
                .flatMap(currencies -> RestApi.getChangellyApi().getMinimumAmount(ChangellyBaseBody.getMinAmount(currencies.get(0).name)), Pair::new)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showLoadCurrenciesProgress(true))
                .doOnUnsubscribe(() -> view.showLoadCurrenciesProgress(false))
                .subscribe(this::onAllCurrenciesReady, this::onAllCurrenciesError);
    }

    private void onAllCurrenciesReady(Pair<ArrayList<ChangellyCurrency>, ChangellyMinimumAmountResponse> responsePair) {
        view.onLoadCurrenciesReady(responsePair.first, responsePair.second);
    }

    private void onAllCurrenciesError(Throwable throwable) {
        view.onLoadCurrenciesFailed(throwable);
    }
}
