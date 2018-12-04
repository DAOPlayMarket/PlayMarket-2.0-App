package com.blockchain.store.playmarket.data.entities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.Constants;

public class PlayMarketUpdateNotification implements NotificationImpl {

    public App app;

    public PlayMarketUpdateNotification(App app) {
        this.app = app;
    }

    public NotificationCompat.Builder createNotification(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_APP_EXTRA, app);
        intent.putExtra(Constants.DOWNLOAD_SERVICE_URL_EXTRA, app.getDownloadLink());
        intent.putExtra(Constants.DOWNLOAD_SERVICE_FORCE_INSTALL, true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_1")
                .setContentTitle(getTitleName())
                .setContentText("Tap here to start update")
                .setSmallIcon(R.mipmap.ic_logo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker("")
                .setContentIntent(pendingIntent);
        return builder;

    }

    @Override
    public int getId() {
        return 987;
    }

    @Override
    public String getTitleName() {
        return "DAO PlayMarket 2.0 is ready to update!";
    }
}
