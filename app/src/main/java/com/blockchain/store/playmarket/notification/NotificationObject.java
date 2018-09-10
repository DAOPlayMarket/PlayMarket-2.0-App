package com.blockchain.store.playmarket.notification;

import android.support.v4.app.NotificationCompat;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.utilities.Constants;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class NotificationObject {
    private Constants.APP_STATE currentState;
    private NotificationImpl item;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCallbacks callback;
    private int progress;

    public NotificationObject() {
    }

    public NotificationObject(NotificationImpl item, Constants.APP_STATE currentState, NotificationCompat.Builder notificationBuilder) {
        this.currentState = currentState;
        this.item = item;
        this.notificationBuilder = notificationBuilder;
    }

    public NotificationImpl getItem() {
        return item;
    }

    public void setItem(NotificationImpl item) {
        this.item = item;
    }

    public NotificationCompat.Builder getNotificationBuilder() {
        return notificationBuilder;
    }

    public void setNotificationBuilder(NotificationCompat.Builder notificationBuilder) {
        this.notificationBuilder = notificationBuilder;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Constants.APP_STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Constants.APP_STATE currentState) {
        this.currentState = currentState;
    }

    public void setCallback(NotificationManagerCallbacks callback) {
        this.callback = callback;
    }

    public NotificationManagerCallbacks getCallback() {
        return callback;
    }
}
