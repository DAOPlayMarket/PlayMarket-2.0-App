package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.util.Pair;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;
import com.blockchain.store.playmarket.data.entities.AppLibrary;
import com.blockchain.store.playmarket.data.entities.AppUpdateNotification;
import com.blockchain.store.playmarket.data.entities.PlayMarketUpdateNotification;
import com.blockchain.store.playmarket.notification.NotificationManager;
import com.blockchain.store.playmarket.repositories.MyAppsRepository;
import com.blockchain.store.playmarket.utilities.MyPackageManager;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.blockchain.store.playmarket.utilities.Constants.DOWNLOAD_NEW_VERSION_WITHOUT_PROMPT;

/**
 * Created by Igor.Sakovich on 17.10.2018.
 */

public class CheckUpdateJobService extends JobService {
    private static final String TAG = "CheckUpdateJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        String s = MyPackageManager.prepareApplicationInfoForRequest();
        new MyAppsRepository().getApps()
                .subscribe(result -> onGetAppsReady(result, params),
                        throwable -> onGetAppsFailed(throwable, params));
        return true;
    }

    private void onGetAppsReady(Pair<ArrayList<App>, Integer> result, JobParameters params) {
        if (!result.first.isEmpty() && result.second != 0) {
            for (App app : result.first) {
                if (app.packageName.equalsIgnoreCase(getBaseContext().getPackageName())
                        && Integer.parseInt(app.version) > BuildConfig.VERSION_CODE) {
                    onPlayMarketNewVersionAvailable(app);
                }
            }

            onAppsUpdateAvailiable(result.second);
        }
        jobFinished(params, false);
    }

    private void onGetAppsFailed(Throwable throwable, JobParameters params) {

        jobFinished(params, true);
        Log.d(TAG, "onGetAppsFailed() called with: throwable = [" + throwable + "], params = [" + params + "]");
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void onPlayMarketNewVersionAvailable(App app) {
        PlayMarketUpdateNotification playMarketUpdateNotification = new PlayMarketUpdateNotification(app);
        NotificationManager.getManager().registerNewNotification(playMarketUpdateNotification);
        if (Hawk.get(DOWNLOAD_NEW_VERSION_WITHOUT_PROMPT, false)) {

        } else {

        }

    }

    private void onAppsUpdateAvailiable(int countOfAppsHasUpdate) {
        AppUpdateNotification playMarketUpdateNotification = new AppUpdateNotification(countOfAppsHasUpdate);
        NotificationManager.getManager().registerNewNotification(playMarketUpdateNotification);
    }


}
