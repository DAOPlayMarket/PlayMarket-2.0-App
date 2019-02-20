package com.blockchain.store.playmarket.interfaces;

import com.blockchain.store.playmarket.data.entities.App;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public interface NotificationManagerCallbacks {
    void onAppDownloadStarted(NotificationImpl app);
    void onAppDownloadProgressChanged(NotificationImpl app, int progress);
    void onAppDownloadSuccessful(NotificationImpl app);
    void onAppDownloadError(NotificationImpl app, String message);
}
