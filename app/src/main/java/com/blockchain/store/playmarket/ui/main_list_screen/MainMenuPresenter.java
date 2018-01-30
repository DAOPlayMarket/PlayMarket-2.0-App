package com.blockchain.store.playmarket.ui.main_list_screen;

import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.BaseInfuraResponse;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.entities.InfuraUserBalanceBody;

import java.util.ArrayList;

import okhttp3.RequestBody;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainMenuContract.*;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class MainMenuPresenter implements Presenter {
    private static final String TAG = "MainMenuPresenter";

    View view;

    @Override
    public void init(View view) {
        this.view = view;
    }

    @Override
    public void loadCategories() {
        RestApi.instance().getCagories().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> view.setProgress(true))
                .doOnTerminate(() -> view.setProgress(false))
                .subscribe(this::onCategoriesLoaded, this::onCategoriesLoadFail);

//        RestApi.getInfuraApi().getUserBalance(new InfuraUserBalanceBody("0x542F5F14cff76c619eE91fada241e053079D3CA3"))
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::onBalanceOk, this::onBalanceFail);

    }

    private void onBalanceOk(BaseInfuraResponse baseInfuraResponse) {
        Log.d(TAG, "onBalanceOk() called with: baseInfuraResponse = [" + baseInfuraResponse + "]");
    }

    private void onBalanceFail(Throwable throwable) {
        Log.d(TAG, "onBalanceFail() called with: throwable = [" + throwable + "]");
    }


    private void onCategoriesLoaded(ArrayList<Category> categories) {
        view.onCategoryLoaded(categories);
    }

    private void onCategoriesLoadFail(Throwable throwable) {
        view.onCategoryLoadFailed(throwable);
    }
}
