
package com.blockchain.store.playmarket.check_transation_status_beta;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.services.DownloadService;
import com.blockchain.store.playmarket.utilities.Constants;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReportAppInstallJob extends JobService {
    private static final String TAG = "ReportAppInstallJob";

    @Override
    public boolean onStartJob(JobParameters params) {
        String appId = params.getExtras().getString(Constants.JOB_APP_ID);
        String userId = DownloadService.getHashedAndroidId(this);
        Log.d(TAG, "onStartJob() called with: idApp: " + appId + " userId " + userId);
        RestApi.getServerApi().reportAppInstallEvent(appId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> onTransactionReady(result, params),
                        throwable -> onTransactionError(throwable, params));
        return false;
    }

    private void onTransactionReady(ResponseBody result, JobParameters params) {
        Log.d(TAG, "onTransactionReady() called with: result = [" + result + "], params = [" + params + "]");
        jobFinished(params, false);
    }

    private void onTransactionError(Throwable throwable, JobParameters params) {
        Log.d(TAG, "onTransactionError() called with: throwable = [" + throwable + "], params = [" + params + "]");
        jobFinished(params, true);
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
