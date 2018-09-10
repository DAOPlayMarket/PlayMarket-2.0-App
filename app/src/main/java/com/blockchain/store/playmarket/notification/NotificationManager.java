package com.blockchain.store.playmarket.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.interfaces.NotificationManagerCallbacks;
import com.blockchain.store.playmarket.utilities.Constants;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Crypton04 on 30.01.2018.
 */

public class NotificationManager {
    private static final String TAG = "NotificationManager";

    private static ArrayList<NotificationObject> notificationObjects;
    private static NotificationManager instance;
    private ArrayList<Pair<Integer, NotificationManagerCallbacks>> callbacks = new ArrayList<>();

    private NotificationManager() {
        notificationObjects = new ArrayList<>();
    }

    public static NotificationManager getManager() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void registerNewNotification(NotificationImpl item) {
        NotificationObject newObject = new NotificationObject(item, Constants.APP_STATE.STATE_DOWNLOADING, createNotification(item));
        showNotification(newObject);
        notificationObjects.add(newObject);
    }

    public void registerCallback(NotificationImpl item, NotificationManagerCallbacks callbacks) {
        this.callbacks.add(Pair.create(item.getId(), callbacks));
        NotificationObject notificationObject = findNotificationObjectByItem(item);
        if (notificationObject != null) {
            Log.d(TAG, "notification object with progress: " + notificationObject.getProgress());
            reportUpdateProgress(notificationObject);
        }
    }

    public NotificationObject findNotificationObjectByItem(NotificationImpl item) {
        for (Pair<Integer, NotificationManagerCallbacks> callback : this.callbacks) {
            if (callback.first == item.getId()) {
                return getNotificationObjectByApp(item);
            }
        }
        return null;
    }

    public boolean isCallbackAlreadyRegistered(NotificationImpl item) {
        NotificationObject notificationObjectByApp = getNotificationObjectByApp(item);
        return notificationObjectByApp != null;
    }

    private NotificationObject getNotificationObjectByApp(NotificationImpl item) {
        for (NotificationObject object : notificationObjects) {
            if (object.getItem().getId() == item.getId()) {
                return object;
            }
        }
        return null;
    }

    public void downloadCompleteWithError(NotificationImpl item, Exception exception) {

        NotificationObject notificationObject = getNotificationObjectByApp(item);
        if (notificationObject != null) {
            notificationObject.setCurrentState(Constants.APP_STATE.STATE_DOWNLOAD_ERROR);
            reportDownloadFailUpdate(notificationObject, exception.getMessage());
            cancelNotification(item);
            updateText(item, exception.getMessage());
            removeNotificationObject(notificationObject);
        }
    }

    public void downloadCompleteWithoutError(NotificationImpl item) {
        NotificationObject notificationObject = getNotificationObjectByApp(item);
        if (notificationObject != null) {
            notificationObject.setCurrentState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
            showNotification(notificationObject);
            reportCompleteUpdate(notificationObject);
            cancelNotification(item);
            removeNotificationObject(notificationObject);
        }
    }

    private void removeNotificationObject(NotificationObject notificationObject) {
        if (notificationObjects.isEmpty()) return;
        notificationObjects.remove(notificationObject);
    }

    public void updateProgress(App app, int progress) {
        NotificationObject notificationObject = getNotificationObjectByApp(app);
        if (notificationObject == null) return;
        if (notificationObject.getProgress() == progress) return;
        notificationObject.setProgress(progress);
        notificationObject.getNotificationBuilder().setProgress(100, progress, false);
        showNotification(notificationObject);
        reportUpdateProgress(notificationObject);
    }

    public void updateText(NotificationImpl item, String text) {
        NotificationObject notificationObject = getNotificationObjectByApp(item);
        if (notificationObject == null) return;
        notificationObject.getNotificationBuilder().setContentText(text);
        showNotification(notificationObject);
    }

    private NotificationCompat.Builder createInstallNotification(App app) {
        Context context = Application.getInstance().getApplicationContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setContentTitle(app.nameApp)
                .setContentText(context.getString(R.string.download_msg))
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
        return notificationBuilder;
    }

    private void cancelNotification(NotificationImpl item) {
        NotificationObject notificationObject = getNotificationObjectByApp(item);
        if (notificationObject == null) return;

        Context context = Application.getInstance().getApplicationContext();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationObject.getNotificationBuilder().setProgress(0, 0, false);
        notificationManager.notify(item.getId(), notificationObject.getNotificationBuilder().build());
    }

    private NotificationCompat.Builder createNotification(NotificationImpl item) {
        Context context = Application.getInstance().getApplicationContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setContentTitle(item.getTitleName())
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("");
        notificationBuilder.setProgress(100, 0, false); // show progress
        return notificationBuilder;
    }

    private void showNotification(NotificationObject notificationObject) {
        Context context = Application.getInstance().getApplicationContext();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationObject.getItem().getId(), notificationObject.getNotificationBuilder().build());
    }


    private void reportUpdateProgress(NotificationObject notificationObject) {
        for (Pair<Integer, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first == notificationObject.getItem().getId()) {
                object.second.onAppDownloadProgressChanged((App) notificationObject.getItem(), notificationObject.getProgress());
            }
        }
    }

    private void reportStartUpdate(NotificationObject notificationObject) {
        for (Pair<Integer, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first == notificationObject.getItem().getId()) {
                object.second.onAppDownloadStarted((App) notificationObject.getItem());
            }
        }
    }

    private void reportCompleteUpdate(NotificationObject notificationObject) {
        for (Pair<Integer, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first == notificationObject.getItem().getId()) {
                object.second.onAppDownloadSuccessful((App) notificationObject.getItem());
            }
        }
    }

    private void reportDownloadFailUpdate(NotificationObject notificationObject, String message) {
        for (Pair<Integer, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first == notificationObject.getItem().getId()) {
                object.second.onAppDownloadError((App) notificationObject.getItem(), message);
            }
        }
    }


    public void removeCallback(App app, NotificationManagerCallbacks callback) {
        if (this.callbacks.isEmpty()) return;
        Log.d(TAG, "removeCallback: was callbacks " + this.callbacks.size());
        for (Iterator<Pair<Integer, NotificationManagerCallbacks>> iterator = this.callbacks.iterator(); iterator.hasNext(); ) {
            Pair<Integer, NotificationManagerCallbacks> object = iterator.next();
            if (object.second != null && object.second.equals(callback)) {
                iterator.remove();
            }
        }
        Log.d(TAG, "removeCallback: now callbacks " + this.callbacks.size());
    }
}

