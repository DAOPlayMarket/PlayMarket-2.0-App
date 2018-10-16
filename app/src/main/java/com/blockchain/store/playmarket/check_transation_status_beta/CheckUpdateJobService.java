package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.App;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Igor.Sakovich on 17.10.2018.
 */

public class CheckUpdateJobService extends JobService {
    private static final String TAG = "CheckUpdateJobService";

    @Override public boolean onStartJob(JobParameters params) {
        RestApi.getServerApi().getAppsByPackage(getBaseContext().getPackageName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> onGetAppsReady(result, params),
                        throwable -> onGetAppsFailed(throwable, params));
        return true;
    }

    private void onGetAppsReady(ArrayList<App> result, JobParameters params) {
        if (!result.isEmpty()) {
            App app = result.get(0);
            if (Integer.parseInt(app.version) < BuildConfig.VERSION_CODE) {
//todo show notification
            }

        }
    }

    private void onGetAppsFailed(Throwable throwable, JobParameters params) {

    }

    @Override public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
