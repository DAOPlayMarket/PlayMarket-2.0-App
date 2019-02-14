package com.blockchain.store.playmarket.check_transation_status_beta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppUpdateNotification;
import com.blockchain.store.playmarket.data.entities.PlayMarketUpdateNotification;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.repositories.MyAppsRepository;
import com.blockchain.store.playmarket.utilities.Constants;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CheckUpdateWorker extends Worker {
    private static final String TAG = "CheckUpdateWorker";

    public CheckUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        initWork();

        return Result.success();
    }

    public void initWork() {
        Log.d(TAG, "Check update worker started");
        String s = MyPackageManager.prepareApplicationInfoForRequest();
        new MyAppsRepository().getApps()
                .subscribe(this::onGetAppsReady,
                        this::onGetAppsFailed);
    }

    private void onGetAppsReady(Pair<ArrayList<App>, Integer> result) {
        if (!result.first.isEmpty() && result.second != 0) {
            for (App app : result.first) {
                if (app.packageName.equalsIgnoreCase(getApplicationContext().getPackageName())
                        && Integer.parseInt(app.version) > BuildConfig.VERSION_CODE) {
                    onPlayMarketNewVersionAvailable(app);
                }
            }

            onAppsUpdateAvailable(result.second);
        }
    }

    private void onGetAppsFailed(Throwable throwable) {
        Log.d(TAG, "onGetAppsFailed() called with: throwable = [" + throwable + "]");
    }


    private void onPlayMarketNewVersionAvailable(App app) {
        if (Hawk.get(Constants.SETTINGS_SHOW_PLAYMARKET_UPDATE_NOTIFICATION, true)) {
            PlayMarketUpdateNotification playMarketUpdateNotification = new PlayMarketUpdateNotification(app);
            NotificationManager.getManager().registerNewNotification(playMarketUpdateNotification);
        }


    }

    private void onAppsUpdateAvailable(int countOfAppsHasUpdate) {
        if (Hawk.get(Constants.SETTINGS_SHOW_UPDATE_NOTIFICATION, true)) {
            AppUpdateNotification playMarketUpdateNotification = new AppUpdateNotification(countOfAppsHasUpdate);
            NotificationManager.getManager().registerNewNotification(playMarketUpdateNotification);
        }

    }
}
