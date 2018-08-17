package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.playmarket.data.entities.AppLibrary;

public interface AppsAdapterCallback {
    void onActionItemClicked(AppLibrary app, int position);
}
