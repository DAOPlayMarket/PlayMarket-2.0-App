package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.playmarket.data.entities.App;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public interface NotificationManagerCallbacks {
    void onAppDownloadStarted(App app);
    void onAppDownloadProgressChanged(App app, int progress);
    void onAppDownloadSuccessful(App app);
    void onAppDownloadError(App app, String message);
}
