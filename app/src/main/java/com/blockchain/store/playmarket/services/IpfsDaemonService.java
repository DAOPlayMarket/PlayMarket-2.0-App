package com.blockchain.store.playmarket.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.ipfs.IPFSDaemon;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IpfsDaemonService extends IntentService {
    private static final String TAG = "IpfsDaemonService";
    public static final String ACTION_START = "IPFS_DAEMON_ACTION";
    public static final String ACTION_STOP = "IPFS_DAEMON_ACTION_STOP";

    public IpfsDaemonService() {
        super("IpfsDaemonService");

    }

    @Override
    public void onCreate() {
        registerChannel();
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable @android.support.annotation.Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent() called with: intent = [" + intent + "]");
        if (intent != null && intent.getAction() != null && intent.getAction().equalsIgnoreCase("STOP")) {
            stopSelf();
        } else {
            sendStartIntent();
            start();
            try {
                IPFSDaemon.run("daemon --enable-gc").waitFor();
                Log.d(TAG, "onHandleIntent: daemon is started");
            } catch (Exception e) {
                onDestroy();
                e.printStackTrace();
            }
        }

    }

    private void sendStartIntent() {
        Intent startIntent = new Intent();
        startIntent.setAction(ACTION_START);
        LocalBroadcastManager.getInstance(this).sendBroadcast(startIntent);
    }

    private void sendStopIntent() {
        Intent startIntent = new Intent();
        startIntent.setAction(ACTION_STOP);
        LocalBroadcastManager.getInstance(this).sendBroadcast(startIntent);
    }


    private void registerChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ipfs-channel", "ipfs-channel", android.app.NotificationManager.IMPORTANCE_DEFAULT);
            android.app.NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    @Override
    public int onStartCommand(@Nullable @android.support.annotation.Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        if (intent != null && intent.getAction() != null && intent.getAction().equalsIgnoreCase("STOP")) {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public NotificationCompat.Builder getNotification() {
        Intent stopIntent = new Intent(this, IpfsDaemonService.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 111, stopIntent, 0);

        return new NotificationCompat.Builder(this, "ipfs-channel")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setShowWhen(false)
                .setContentTitle(getString(R.string.ipfs_daemon_title))
                .setContentText(getString(R.string.ipfs_daemon_body))
                .addAction(android.R.drawable.ic_notification_clear_all, getString(R.string.ipfs_daemon_stop_btn), stopPendingIntent);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        RestApi.getServerApi().shutdownIpfs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onShutdown, this::onShutDownError);

    }

    private void onShutdown(ResponseBody responseBody) {
        IPFSDaemon.setIpfsProcess(null);
        sendStopIntent();
        NotificationManagerCompat.from(this).cancel(Constants.IPFS_NOTIFICATION_ID);
        super.onDestroy();
    }

    private void onShutDownError(Throwable throwable) {
        Log.d(TAG, "onShutDownError() called with: throwable = [" + throwable + "]");
        sendStopIntent();
        NotificationManagerCompat.from(this).cancel(Constants.IPFS_NOTIFICATION_ID);
        super.onDestroy();
    }

    private void start() {
        NotificationCompat.Builder notification = getNotification();
        startForeground(Constants.IPFS_NOTIFICATION_ID, notification.build());
    }
}
