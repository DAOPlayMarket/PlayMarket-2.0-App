package com.blockchain.store.playmarket.ui.search_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.SearchResponse;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Crypton04 on 12.02.2018.
 */

public class SearchPresenter implements SearchContract.Presenter {
    SearchContract.View view;

    @Override
    public void init(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void searchQuery(String searchQuery) {
        RestApi.getServerApi().getSearchResult(searchQuery)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.showProgress(true))
                .doOnTerminate(() -> view.showProgress(false))
                .subscribe(this::onSearchResultReady, this::onSearchResultFail);
    }


    private void onSearchResultReady(SearchResponse searchResponse) {
        view.onSearchResultReady(searchResponse.apps);

    }

    private void onSearchResultFail(Throwable throwable) {
        view.onSearchResultFail(throwable);
    }
}
