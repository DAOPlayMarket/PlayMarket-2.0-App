package com.blockchain.store.playmarket.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.blockchain.store.playmarket.Application;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class NotificationManager {
    public void createNotification(String id, String appName){
        Context context = Application.getInstance().getApplicationContext();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setContentTitle(appName);

//        notificationBuilder.setProgress(100,0,false); show progress
//        notificationBuilder.setProgress(0,0,false) removes progres bar
//        notificationManager.notify(0,notificationBuilder.build()); // update notification
//        notificationManager.cancel(); // remove notification



    }
}
