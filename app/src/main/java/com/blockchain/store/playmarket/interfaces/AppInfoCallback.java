package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.playmarket.data.entities.AppInfo;

public interface AppInfoCallback {
    void onAppInfoClicked(AppInfo appinfo);
    void onAppTransferTokenClicked(AppInfo appinfo);

    default void onAppInvestClicked(String address) {

    }
}
