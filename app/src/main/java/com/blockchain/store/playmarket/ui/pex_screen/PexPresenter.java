package com.blockchain.store.playmarket.ui.pex_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PexHistory;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.pex_screen.PexContract.*;

public class PexPresenter implements Presenter {

    View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void loadHistory() {
        RestApi.getServerApi().getPexHistory().
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::onOk,this::onError);
    }

    private void onOk(PexHistory pexHistory) {
        view.onHistoryReady(pexHistory);
    }

    private void onError(Throwable throwable) {

    }
}
