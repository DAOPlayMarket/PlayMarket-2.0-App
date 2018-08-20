package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.playmarket.data.entities.AppLibrary;

public interface AppsAdapterCallback {
    void onActionButtonClicked(AppLibrary app, int position);
    void onLayoutClicked(int numberOfSelectedItem);
}
