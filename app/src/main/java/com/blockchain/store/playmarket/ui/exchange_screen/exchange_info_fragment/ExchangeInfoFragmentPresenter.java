package com.blockchain.store.playmarket.ui.exchange_screen.exchange_info_fragment;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.ChangellyBaseBody;
import com.blockchain.store.playmarket.data.entities.ChangellyCurrency;
import com.blockchain.store.playmarket.data.entities.ChangellyMinimumAmountResponse;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 27.02.2018.
 */

public class ExchangeInfoFragmentPresenter implements ExchangeInfoFragmentContract.Presenter {
    private static final String TAG = "ExchangeActivityPresent";
    private ExchangeInfoFragmentContract.View view;

    @Override
    public void init(ExchangeInfoFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void getEstimatedAmount(String name, String amount) {
        RestApi.getChangellyApi().getExchangeAmount(ChangellyBaseBody.getExchangeAmount(name, amount))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showLoadCurrenciesProgress(true))
                .doOnUnsubscribe(() -> view.showLoadCurrenciesProgress(false))
                .subscribe(this::onEstimatedAmountReady, this::onEstimatedAmountFail);
    }

    private void onEstimatedAmountReady(ChangellyMinimumAmountResponse changellyMinimumAmountResponse) {
        view.onEstimatedAmountReady(changellyMinimumAmountResponse.result);
    }

    private void onEstimatedAmountFail(Throwable throwable) {
        view.onEstimatedAmountFail(throwable);
    }

    @Override
    public void loadMinimumAmount(ChangellyCurrency changellyCurrency) {
        RestApi.getChangellyApi().getMinimumAmount(ChangellyBaseBody.getMinAmount(changellyCurrency.name))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showLoadCurrenciesProgress(true))
                .doOnUnsubscribe(() -> view.showLoadCurrenciesProgress(false))
                .subscribe(this::onMinimumAmountReady, this::onMinimumAmountFailed);

    }

    private void onMinimumAmountReady(ChangellyMinimumAmountResponse changellyMinimumAmountResponse) {
        view.onMinimumAmountReady(changellyMinimumAmountResponse.result);
    }

    private void onMinimumAmountFailed(Throwable throwable) {
        view.onMinimumAmountError(throwable);
    }


}
