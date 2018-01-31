package com.blockchain.store.playmarket.interfaces;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public interface NotificationManagerCallbacks {
    void onAppDownloadStarted();
    void onAppDownloadProgressChanged(int progress);
    void onAppDownloadSuccessful();
    void onAppDownloadError();
}
