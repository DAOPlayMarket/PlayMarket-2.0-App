package com.blockchain.store.playmarket.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.data.entities.App;
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
    private ArrayList<Pair<String, NotificationManagerCallbacks>> callbacks = new ArrayList<>();

    private NotificationManager() {
        notificationObjects = new ArrayList<>();
    }

    public static NotificationManager getManager() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public void registerNewNotification(App app) {
        NotificationObject newObject = new NotificationObject(app, Constants.APP_STATE.STATE_DOWNLOADING, createNotification(app));
        showNotification(newObject);
        notificationObjects.add(newObject);
    }

    public void registerCallback(App app, NotificationManagerCallbacks callbacks) {
        this.callbacks.add(Pair.create(app.appId, callbacks));
        for (Pair<String, NotificationManagerCallbacks> callback : this.callbacks) {
            if (callback.first.equalsIgnoreCase(app.appId)) {
                NotificationObject notificationObject = getNotificationObjectByApp(app);
                if (notificationObject != null) {
                    Log.d(TAG, "notification object with progress: " + notificationObject.getProgress());
                    reportUpdateProgress(notificationObject);
                }
            }
        }
    }

    public boolean isCallbackAlreadyRegistered(App app, NotificationManagerCallbacks callbacks) {
        Log.d(TAG, "isCallbackAlreadyRegistered: " + notificationObjects);
        for (NotificationObject notificationObject : notificationObjects) {
            if (notificationObject.getApp().appId.equals(app.appId)) {
                return true;
            }
        }
        return false;
    }

    public void downloadCompleteWithError(App app) {

        NotificationObject notificationObject = getNotificationObjectByApp(app);
        if (notificationObject != null) {
            notificationObject.setCurrentState(Constants.APP_STATE.STATE_DOWNLOAD_ERROR);
            reportDownloadFailUpdate(notificationObject);
            cancelNotification(app);
            removeNotificationObject(notificationObject);
        }
    }

    public void downloadCompleteWithoutError(App app) {
        NotificationObject notificationObject = getNotificationObjectByApp(app);
        if (notificationObject != null) {
            notificationObject.setCurrentState(Constants.APP_STATE.STATE_DOWNLOADED_NOT_INSTALLED);
//            notificationObject.setNotificationBuilder(createInstallNotification(app));
            showNotification(notificationObject);
            reportCompleteUpdate(notificationObject);
            cancelNotification(app);
            removeNotificationObject(notificationObject);
        }
    }

    private void removeNotificationObject(NotificationObject notificationObject) {
        if (notificationObjects.isEmpty()) return;
        Log.d(TAG, "removeNotificationObject: was  " + notificationObjects.size());
        notificationObjects.remove(notificationObject);
        Log.d(TAG, "removeNotificationObject: now  " + notificationObjects.size());
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

    private NotificationCompat.Builder createInstallNotification(App app) {
        Context context = Application.getInstance().getApplicationContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setContentTitle(app.nameApp)
                .setContentText(context.getString(R.string.download_msg))
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
        return notificationBuilder;
    }

    private void cancelNotification(App app) {
        NotificationObject notificationObject = getNotificationObjectByApp(app);
        if (notificationObject == null) return;

        Context context = Application.getInstance().getApplicationContext();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationObject.getNotificationBuilder().setProgress(0, 0, false);
        notificationManager.notify(Integer.parseInt(app.appId), notificationObject.getNotificationBuilder().build());
    }

    private NotificationCompat.Builder createNotification(App app) {
        Context context = Application.getInstance().getApplicationContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setContentTitle(app.nameApp)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon);
        notificationBuilder.setProgress(100, 0, false); // show progress
        return notificationBuilder;
    }

    private void showNotification(NotificationObject notificationObject) {
        Context context = Application.getInstance().getApplicationContext();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.parseInt(notificationObject.getApp().appId), notificationObject.getNotificationBuilder().build());
    }

    private NotificationObject getNotificationObjectByApp(App app) {
        for (NotificationObject object : notificationObjects) {
            if (object.getApp().appId.equalsIgnoreCase(app.appId)) {
                return object;
            }
        }
        return null;
    }


    private void reportUpdateProgress(NotificationObject notificationObject) {
        for (Pair<String, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first.equalsIgnoreCase(notificationObject.getApp().appId)) {
                object.second.onAppDownloadProgressChanged(notificationObject.getProgress());
            }
        }
    }

    private void reportStartUpdate(NotificationObject notificationObject) {
        for (Pair<String, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first.equalsIgnoreCase(notificationObject.getApp().appId)) {
                object.second.onAppDownloadStarted();
            }
        }
    }

    private void reportCompleteUpdate(NotificationObject notificationObject) {
        for (Pair<String, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first.equalsIgnoreCase(notificationObject.getApp().appId)) {
                object.second.onAppDownloadSuccessful();
            }
        }
    }

    private void reportDownloadFailUpdate(NotificationObject notificationObject) {
        for (Pair<String, NotificationManagerCallbacks> object : this.callbacks) {
            if (object.first.equalsIgnoreCase(notificationObject.getApp().appId)) {
                object.second.onAppDownloadError();
            }
        }
    }


    public void removeCallback(App app, NotificationManagerCallbacks callback) {
        if (this.callbacks.isEmpty()) return;
        Log.d(TAG, "removeCallback: was callbacks " + this.callbacks.size());
        for (Iterator<Pair<String, NotificationManagerCallbacks>> iterator = this.callbacks.iterator(); iterator.hasNext(); ) {
            Pair<String, NotificationManagerCallbacks> object = iterator.next();
            if (object.second != null && object.second.equals(callback)) {
                iterator.remove();
            }
        }
//        for (Pair<String, NotificationManagerCallbacks> object : this.callbacks) {
//            if (object.second != null && object.second.equals(callback)) {
//                this.callbacks.remove(object);
//            }
//        }
        Log.d(TAG, "removeCallback: now callbacks " + this.callbacks.size());
    }
}

