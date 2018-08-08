package com.blockchain.store.playmarket.ui.my_apps_screen;

public class MyAppsPresenter implements MyAppsContract.Presenter {
    private MyAppsContract.View view;

    @Override
    public void init(MyAppsContract.View view) {
        this.view = view;
    }
}
