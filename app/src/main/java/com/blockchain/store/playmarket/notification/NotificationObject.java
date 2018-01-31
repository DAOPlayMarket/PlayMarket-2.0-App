package com.blockchain.store.playmarket.notification;

import android.support.v4.app.NotificationCompat;

import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.utilities.Constants;

/**
 * Created by Crypton04 on 31.01.2018.
 */

public class NotificationObject {
    private Constants.DOWNLOAD_STATE currentState;
    private App app;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCallbacks callback;
    private int progress;

    public NotificationObject() {
    }

    public NotificationObject(App app, Constants.DOWNLOAD_STATE currentState, NotificationCompat.Builder notificationBuilder) {
        this.currentState = currentState;
        this.app = app;
        this.notificationBuilder = notificationBuilder;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
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

    public Constants.DOWNLOAD_STATE getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Constants.DOWNLOAD_STATE currentState) {
        this.currentState = currentState;
    }

    public void setCallback(NotificationManagerCallbacks callback) {
        this.callback = callback;
    }

    public NotificationManagerCallbacks getCallback() {
        return callback;
    }
}
