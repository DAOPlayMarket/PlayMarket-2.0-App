package com.blockchain.store.playmarket.ui.main_list_screen;

import android.util.Log;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;
import com.blockchain.store.playmarket.data.entities.Category;
import com.blockchain.store.playmarket.data.listeners.AppDispatcherListeners;

import java.util.ArrayList;

import static com.blockchain.store.playmarket.ui.main_list_screen.MainFragmentContract.*;

/**
 * Created by Crypton04 on 26.01.2018.
 */

public class MainFragmentPresenter implements Presenter, AppDispatcherListeners {
    private static final String TAG = "MainFragmentPresenter";
    ArrayList<AppDispatcherType> appDispatcherTypes;
    private View view;

    @Override
    public void init(View view) {
        this.view = view;

    }

    @Override
    public void setProvider(Category category) {
        appDispatcherTypes = Application.getAppsDispatcher().addProviders(category);
        view.onDispatchersReady(appDispatcherTypes);
    }


    @Override
    public void addItemCountChanged(ArrayList<App> apps) {
        Log.d(TAG, "addItemCountChanged() called with: apps = [" + apps + "]");
    }

    @Override
    public void onNewItemError(Throwable throwable) {
        Log.d(TAG, "onNewItemError() called with: throwable = [" + throwable + "]");
    }

    @Override
    public void onNewItemCountChanged(AppDispatcherType dispatcherType) {
        Log.d(TAG, "onNewItemCountChanged() called with: dispatcherType = [" + dispatcherType + "]");
        view.firstItemsReady(dispatcherType);
    }

    @Override
    public void onDestroy() {
        Application.getAppsDispatcher().removeListener(this);
    }

    @Override
    public void loadNewData(AppDispatcherType dispatcherType) {
        Application.getAppsDispatcher().loadNewData(dispatcherType, this);
    }

    @Override
    public void requestNewItems(AppDispatcherType appDispatcherType) {
        if (!appDispatcherType.isInitialized) {
            appDispatcherType.isInitialized = true;
            Application.getAppsDispatcher().loadNewData(appDispatcherType, this);
        }
    }
}
