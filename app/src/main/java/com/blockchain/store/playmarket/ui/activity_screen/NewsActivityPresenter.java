package com.blockchain.store.playmarket.ui.activity_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.PlaymarketFeed;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.activity_screen.NewsActivityContract.Presenter;
import static com.blockchain.store.playmarket.ui.activity_screen.NewsActivityContract.View;

public class NewsActivityPresenter implements Presenter {
    private View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void getNews() {
        RestApi.getXmlApi().getPlaymarketNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onNewsReady, this::onNewsFailed);
    }

    private void onNewsReady(PlaymarketFeed playmarketFeed) {
        view.onNewsReady(playmarketFeed);
    }

    private void onNewsFailed(Throwable throwable) {
        view.onNewsError(throwable);
    }
}
