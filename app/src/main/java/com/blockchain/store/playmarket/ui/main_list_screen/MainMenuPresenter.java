package com.blockchain.store.playmarket.ui.main_list_screen;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.Category;

import java.util.ArrayList;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainMenuContract.*;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class MainMenuPresenter implements Presenter {
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
    }

    private void onCategoriesLoaded(ArrayList<Category> categories) {
        view.onCategoryLoaded(categories);
    }

    private void onCategoriesLoadFail(Throwable throwable) {
        view.onCategoryLoadFailed(throwable);
    }
}
