package com.blockchain.store.playmarket.data.entities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.interfaces.NotificationImpl;
import com.blockchain.store.playmarket.ui.main_list_screen.MainMenuActivity;
import com.blockchain.store.playmarket.utilities.Constants;

public class AppUpdateNotification implements NotificationImpl {

    public int updateCount;

    public AppUpdateNotification(int updateCount) {
        this.updateCount = updateCount;
    }

    public NotificationCompat.Builder createNotification(Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.OPEN_MY_APPS_EXTRA, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_1")
                .setContentTitle(getTitleName())
                .setContentText(updateCount + " apps are ready to update")
                .setSmallIcon(R.drawable.ic_pm_logo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setTicker("")
                .setContentIntent(pendingIntent);
        return builder;

    }

    @Override
    public int getId() {
        return 986;
    }

    @Override
    public String getTitleName() {
        return "Updates available!";
    }
}
