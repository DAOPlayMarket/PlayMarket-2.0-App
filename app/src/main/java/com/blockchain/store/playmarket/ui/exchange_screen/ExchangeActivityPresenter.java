package com.blockchain.store.playmarket.ui.exchange_screen;

import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyCreateTransactionResponse;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ExchangeActivityPresenter implements ExchangeActivityContract.Presenter {
    private static final String TAG = "ExchangeActivityPresent";
    private ExchangeActivityContract.View view;

    @Override
    public void init(ExchangeActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void loadAllCurrencies() {
        RestApi.getChangellyApi().getCurrenciesFull(ChangellyBaseBody.getCurrenciesFull())
                .map(changellyCurrenciesResponse -> {
                    ArrayList<ChangellyCurrency> changellyCurrencies = new ArrayList<>();
                    for (ChangellyCurrency currency : changellyCurrenciesResponse.result) {
                        if (currency.enabled) {
                            changellyCurrencies.add(currency);
                        }
                    }
                    return changellyCurrencies;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showLoadCurrenciesProgress(true))
                .doOnTerminate(() -> view.showLoadCurrenciesProgress(false))
                .subscribe(this::onAllCurrenciesReady, this::onAllCurrenciesError);
    }

    private void onAllCurrenciesReady(ArrayList<ChangellyCurrency> changellyCurrencies) {
        view.onLoadCurrenciesReady(changellyCurrencies);
    }

    private void onAllCurrenciesError(Throwable throwable) {
        view.onLoadCurrenciesFailed(throwable);
    }

    @Override
    public void getEstimatedAmount(String name, String amount) {
        RestApi.getChangellyApi().getExchangeAmount(ChangellyBaseBody.getExchangeAmount(name, amount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEstimatedAmountReady, this::onEstimatedAmountFail);
    }

    @Override
    public void createTransaction(String from, String address, String amount, String extraId) {
        RestApi.getChangellyApi().createTransaction(ChangellyBaseBody.createTransactionBody(from, address, amount, extraId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCreateTransationReady, this::onCreateTransactionFailed);
    }

    private void onCreateTransationReady(ChangellyCreateTransactionResponse changellyCreateTransactionResponse) {
        Log.d(TAG, "onCreateTransationReady() called with: changellyCreateTransactionResponse = [" + changellyCreateTransactionResponse + "]");
    }

    private void onCreateTransactionFailed(Throwable throwable) {
        Log.d(TAG, "onCreateTransactionFailed() calledwith: throwable = [" + throwable + "]");
    }

    private void onEstimatedAmountReady(ChangellyMinimumAmountResponse changellyMinimumAmountResponse) {
        view.onEstimatedAmountReady(changellyMinimumAmountResponse.result);
    }

    private void onEstimatedAmountFail(Throwable throwable) {
        view.onEstimatedAmountFail(throwable);
    }
}
