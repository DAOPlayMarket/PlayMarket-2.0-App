package com.blockchain.store.playmarket.data.listeners;

import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

/**
 * Created by Crypton04 on 29.01.2018.
 */

public interface AppDataListeners {

    void onNewItemCountChanged(int count, ArrayList<App> apps);
}
