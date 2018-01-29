package com.blockchain.store.playmarket.data.listeners;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppDispatcherType;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public interface AppDispatcherListeners {
    void addItemCountChanged(ArrayList<App> apps);

    void onNewItemError(Throwable throwable);

    void onNewItemCountChanged(AppDispatcherType dispatcherType);
}
